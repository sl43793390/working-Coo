package com.sl.project;

import java.time.LocalDate;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Project;
import com.sl.entity.User;
import com.sl.service.ProjectProcess;
import com.sl.user.UserManagementEnums;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;
import com.sl.util.Util;

import java.time.LocalDate;

/**
 * 全部项目（卡片墙重设计）。
 * <p>
 * 参考 Teambition 的项目列表：搜索栏 + 响应式卡片墙，每张卡片
 * 自带状态徽章、完成进度条、起止时间与操作按钮，替代原来的纯表格。
 * 数据仍走 getProjectByConditionPaged 分页接口，首屏只取一页，
 * 「加载更多」追加下一页，不一次性拉全量。
 */
@SpringComponent
@Scope("prototype")
public class MyProject extends VerticalLayout {

	private static final long serialVersionUID = -9006226841577552848L;
	private static final Logger log = LoggerFactory.getLogger(MyProject.class);

	/** 每次取多少张卡片 */
	private static final int PAGE_SIZE = 24;

	ProjectProcess projectProcess;
	private TextField pname;
	private ComboBox<String> prior;
	private Button searchBtn;
	private Button createBtn;
	private Div cardGrid;
	private Button moreBtn;
	private ConfirmDialog dialog;
	private Project selectedProject;

	/** 拷贝项目弹窗：先让用户改名称、时间、状态等信息，再入库 */
	private Dialog copyDialog;
	private TextField copyName;
	private DatePicker copyStart;
	private DatePicker copyEnd;
	private ComboBox<String> copyStatus;
	private ComboBox<String> copyPrior;
	private Checkbox copyPrivate;
	private Project copySource;

	/** 当前筛选条件，分页取数都会用到 */
	private Project searchCondition = new Project();
	/** 列表空态提示 */
	private Span emptyHint;
	/** 已加载/总数，控制「加载更多」的显隐 */
	private int loadedCount = 0;
	private int totalCount = 0;

	public MyProject(@Autowired ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
		addClassName("list-view");
		setDefaultHorizontalComponentAlignment(Alignment.START);
		setMargin(false);

		HorizontalLayout tile = ComFactory.getTitle("全部项目");
		Span horizontalLine = ComFactory.getHorizontalLine();
		add(tile, horizontalLine);

		initFilterLayout();

		// 删除确认框：服务端会校验「管理员或创建人」，不通过时抛 IllegalArgumentException
		dialog = ComFactory.getConfirmationDialog(conf -> {
			try {
				projectProcess.deleteProjectLogic(selectedProject);
				renderCards(true);
				ComFactory.notificationSuccess("删除成功");
			} catch (Exception ex) {
				log.error("删除项目失败, idProject={}", selectedProject.getIdProject(), ex);
				ComFactory.notificationError(null != ex.getMessage() ? ex.getMessage() : "删除失败，请重试");
			} finally {
				dialog.close();
			}
		}, canc -> dialog.close());

		cardGrid = new Div();
		cardGrid.addClassName("proj-card-grid");
		add(cardGrid);

		moreBtn = ComFactory.getTertriayBtn("加载更多");
		moreBtn.addClickListener(e -> renderCards(false));
		HorizontalLayout moreBar = new HorizontalLayout(moreBtn);
		moreBar.setWidthFull();
		moreBar.setJustifyContentMode(JustifyContentMode.CENTER);
		moreBar.setPadding(false);
		add(moreBar);

		emptyHint = ComFactory.getEmptyHint("暂无项目，可点击右上角「新增项目」创建，或联系管理员把你加入项目");
		add(emptyHint);

		renderCards(true);
	}

	/** 搜索栏 + 新增入口 */
	private void initFilterLayout() {
		HorizontalLayout filterLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		filterLayout.setAlignItems(Alignment.CENTER);
		add(filterLayout);

		pname = ComFactory.getTextField();
		pname.setPlaceholder("按项目名称搜索");
		pname.setClearButtonVisible(true);
		// 回车直接搜，不用非得点按钮
		pname.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> callConditionSearch());
		filterLayout.add(pname);

		prior = ComFactory.getCombobox();
		prior.setItems("高", "中", "低", "全部");
		prior.setValue("全部");
		filterLayout.add(prior);

		searchBtn = ComFactory.getPrimaryBtn("查询");
		searchBtn.addClickListener(e -> callConditionSearch());
		filterLayout.add(searchBtn);

		// 新增入口放在搜索栏另一端，创建路径一步直达
		createBtn = ComFactory.getPrimaryBtn("新增项目");
		createBtn.addClickListener(e -> {
			ProjectCreateComponent createComponent = ComponentUtil.applicationContext.getBean(ProjectCreateComponent.class);
			TabsUtil.addComponentTab("新增项目", "新增项目", createComponent);
		});
		HorizontalLayout spacer = new HorizontalLayout(createBtn);
		spacer.setPadding(false);
		spacer.setSpacing(false);
		spacer.getStyle().set("margin-left", "auto");
		filterLayout.add(spacer);
	}

	private void callConditionSearch() {
		Project p = new Project();
		if (null != pname.getValue() && !pname.getValue().isBlank()) {
			p.setNameProject(pname.getValue().trim());
		}
		if (null != prior.getValue() && !"全部".equals(prior.getValue())) {
			p.setCdPrior(prior.getValue());
		}
		this.searchCondition = p;
		renderCards(true);
	}

	/**
	 * 卡片墙取数：reset=true 时从头加载第一页（搜索/删改后），否则追加下一页。
	 */
	private void renderCards(boolean reset) {
		if (reset) {
			cardGrid.removeAll();
			loadedCount = 0;
		}
		boolean admin = SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER);
		Project cond = this.searchCondition;
		try {
			List<Project> page = projectProcess.getProjectByConditionPaged(cond, admin, loadedCount, PAGE_SIZE);
			for (Project p : page) {
				cardGrid.add(buildCard(p));
			}
			loadedCount += page.size();
			totalCount = (int) projectProcess.countProjectByCondition(cond, admin);
		} catch (Exception e) {
			log.error("加载项目列表失败", e);
			ComFactory.notificationError("加载项目列表失败，请刷新重试");
		}
		moreBtn.setVisible(loadedCount < totalCount);
		ComFactory.toggleEmptyHint(emptyHint, totalCount);
	}

	/* ==================================================================
	 * 项目卡片
	 * ================================================================== */
	private Div buildCard(Project p) {
		Div card = new Div();
		card.addClassName("proj-card");

		// 头部：名称 + 状态徽章
		Div head = new Div();
		head.addClassName("proj-card-head");
		Span name = new Span(p.getNameProject());
		name.addClassName("proj-card-name");
		name.setTitle(p.getNameProject());
		head.add(name, statusBadge(p.getCdStatus()));
		card.add(head);

		// 完成进度条
		double pct = null == p.getPctComplete() ? 0 : p.getPctComplete();
		Div track = new Div();
		track.addClassName("proj-progress");
		Span fill = new Span();
		fill.addClassName("proj-progress-fill");
		fill.getStyle().set("width", Math.max(0, Math.min(100, pct)) + "%");
		track.add(fill);
		Span pctLabel = new Span((int) pct + "%");
		pctLabel.addClassName("proj-progress-label");
		Div progressRow = new Div(track, pctLabel);
		progressRow.addClassName("proj-progress-row");
		card.add(progressRow);

		// 元信息：优先级 / 创建人 / 起止时间 / 私有
		Div meta = new Div();
		meta.addClassName("proj-card-meta");
		Span priorLine = priorBadge(p.getCdPrior());
		priorLine.setTitle("优先级");
		meta.add(priorLine);
		meta.add(metaItem("创建人", p.getCdCreateUser()));
		String range = (null == p.getDtStart() ? "—" : DateUtil.formatDate(p.getDtStart())) + " ~ "
				+ (null == p.getDtEnd() ? "—" : DateUtil.formatDate(p.getDtEnd()));
		Span time = metaItem("起止", range);
		time.setTitle(range);
		meta.add(time);
		if ("Y".equals(p.getFlagPrivate())) {
			Span priv = new Span("私有");
			priv.setClassName("prj-badge prj-badge-purple");
			meta.add(priv);
		}
		card.add(meta);

		// 操作区：权限口径与服务端一致（管理员或创建人可删除）
		Div foot = new Div();
		foot.addClassName("proj-card-foot");
		Button updateBtn = ComFactory.getTertriayBtn("查看/修改");
		updateBtn.addClickListener(e -> {
			ProjectCreateComponent projectCreateComponent = ComponentUtil.applicationContext.getBean(ProjectCreateComponent.class);
			projectCreateComponent.setProject(p);
			projectCreateComponent.setUpdateFlag(true);
			// key 带上项目 id：不同项目各开一个标签页，同一项目重复点击只激活不新增
			TabsUtil.addComponentTab("修改/查看项目-" + p.getIdProject(), "修改/查看项目", projectCreateComponent);
		});
		Button copyBtn = ComFactory.getTertriayBtn("拷贝");
		copyBtn.addClickListener(e -> openCopyDialog(p));
		foot.add(updateBtn, copyBtn);
		if (canDelete(p)) {
			Button deleteBtn = ComFactory.getTertriayBtn("删除");
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
			deleteBtn.addClickListener(e -> {
				this.selectedProject = p;
				dialog.open();
			});
			foot.add(deleteBtn);
		}
		card.add(foot);
		return card;
	}

	private Span metaItem(String label, String value) {
		Span s = new Span(label + "：" + (null == value || value.isBlank() ? "—" : value));
		s.addClassName("proj-meta-item");
		return s;
	}

	/** 项目状态徽章：进行中蓝 / 已完成绿 / 暂停橙 / 未开始灰 */
	private static Span statusBadge(String status) {
		String key = null == status || status.isBlank() ? "未开始" : status;
		String color = switch (key) {
			case "进行中" -> "prj-badge-blue";
			case "已完成", "已结束" -> "prj-badge-green";
			case "暂停" -> "prj-badge-orange";
			default -> "prj-badge-gray";
		};
		Span s = new Span(key);
		s.setClassName("prj-badge " + color);
		return s;
	}

	/** 优先级：色点 + 文案（与任务统计页同一套配色） */
	private static Span priorBadge(String prior) {
		String key = null == prior || prior.isBlank() ? "低" : prior;
		String color = switch (key) {
			case "高" -> "prj-prior-high";
			case "中" -> "prj-prior-mid";
			default -> "prj-prior-low";
		};
		Span dot = new Span();
		dot.setClassName("prj-prior-dot " + color);
		Span text = new Span(key);
		Span wrap = new Span(dot, text);
		wrap.setClassName("prj-prior");
		return wrap;
	}

	/**
	 * 删除权限：管理员，或项目创建人本人（服务端 checkProjectDeletePermission 同口径兜底）。
	 */
	private boolean canDelete(Project p) {
		User u = ComponentUtil.getCurrentUser();
		String me = (null == u) ? null : u.getUserId();
		if (null == me) {
			return false;
		}
		try {
			if (SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return me.equals(p.getCdCreateUser());
	}

	/**
	 * 拷贝项目弹窗：预填原项目信息，用户确认名称、起止时间、状态等后再入库。
	 */
	private void openCopyDialog(Project source) {
		if (null == copyDialog) {
			initCopyDialog();
		}
		this.copySource = source;
		copyName.setValue(source.getNameProject() + "-拷贝");
		copyStart.setValue(null == source.getDtStart() ? LocalDate.now() : Util.dateToLocalDate(source.getDtStart()));
		copyEnd.setValue(null == source.getDtEnd() ? LocalDate.now().plusMonths(1) : Util.dateToLocalDate(source.getDtEnd()));
		copyStatus.setValue(null == source.getCdStatus() || source.getCdStatus().isBlank() ? "未开始" : source.getCdStatus());
		copyPrior.setValue(null == source.getCdPrior() || source.getCdPrior().isBlank() ? "低" : source.getCdPrior());
		copyPrivate.setValue("Y".equals(source.getFlagPrivate()));
		copyDialog.open();
	}

	private void initCopyDialog() {
		VerticalLayout form = ComFactory.getVerticalLayout();
		form.setWidth("100%");
		copyName = ComFactory.getStandardTextField("项目名称：", "410px");
		ComFactory.bindRequired(copyName, "请填写项目名称");
		copyStart = ComFactory.getDateField("开始时间", LocalDate.now(), "410px");
		copyEnd = ComFactory.getDateField("结束时间", LocalDate.now().plusMonths(1), "410px");
		copyStatus = ComFactory.getStandardComboBox("状态：", "410px");
		copyStatus.setItems("未开始", "进行中", "暂停", "已完成");
		copyPrior = ComFactory.getStandardComboBox("优先级：", "410px");
		copyPrior.setItems("高", "中", "低");
		copyPrivate = ComFactory.getCheckbox("是否私有");
		form.add(copyName, copyStart, copyEnd, copyStatus, copyPrior, copyPrivate);

		Button saveBtn = ComFactory.getPrimaryBtn("确认拷贝");
		Button cancelBtn = ComFactory.getTertriayBtn("取消");
		copyDialog = ComFactory.getDialog("拷贝项目", form, saveBtn, cancelBtn);
		copyDialog.setWidth("500px");
		saveBtn.addClickListener(e -> {
			if (!ComFactory.validateFields(copyName)) {
				ComFactory.notificationError("请先修正表单中标红的项");
				return;
			}
			Project custom = new Project();
			custom.setNameProject(copyName.getValue().trim());
			if (copyStart.getOptionalValue().isPresent()) {
				custom.setDtStart(DateUtil.parseDate(copyStart.getValue().toString()));
			}
			if (copyEnd.getOptionalValue().isPresent()) {
				custom.setDtEnd(DateUtil.parseDate(copyEnd.getValue().toString()));
			}
			custom.setCdStatus(copyStatus.getValue());
			custom.setCdPrior(copyPrior.getValue());
			custom.setFlagPrivate(copyPrivate.getValue() ? "Y" : "N");
			if (projectProcess.cloneProject(copySource, custom)) {
				copyDialog.close();
				renderCards(true);
				ComFactory.notification("拷贝成功");
			} else {
				ComFactory.notificationError("拷贝失败，请重试");
			}
		});
		cancelBtn.addClickListener(e -> copyDialog.close());
	}

	public ProjectProcess getProjectProcess() {
		return projectProcess;
	}

	public void setProjectProcess(ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
	}

}
