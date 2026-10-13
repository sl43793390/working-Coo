package com.sl.app.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.MainTabsHolder;
import com.sl.app.component.util.Refreshable;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Menu;
import com.sl.entity.Project;
import com.sl.entity.ProjectUser;
import com.sl.notify.NotificationBell;
import com.sl.project.MyProject;
import com.sl.service.MenuProcess;
import com.sl.service.ProjectProcess;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
/**
 * 左侧是accordion可展开的类似手风琴样式，可隐藏。右侧是tabsheet;
 * @author Administrator
 *
 */
/**
 * 注意：这里只保留 @Route。
 * 原先还标了 @SpringComponent + @UIScope，导致路由实例由 Spring 容器托管，
 * 多个浏览器会话会拿到同一个实例（TabsUtil 通过 getBean 取主 TabSheet），存在跨 UI 共享风险。
 * 现在 TabSheet 通过 MainTabsHolder 绑定到各自的 UI 上，天然按会话隔离。
 */
@Route("projectView")
@PreserveOnRefresh
public class AppLayoutAccordionView extends AppLayout {

	private static final long serialVersionUID = 5988417052518848565L;

	private static final Logger log = LoggerFactory.getLogger(AppLayoutAccordionView.class);

	private TabSheet mainTabsheet;
	private ComboBox<Project> projectBox;
	 ProjectProcess projectProcess;
	 MenuProcess menuProcess;
	 
	public AppLayoutAccordionView(@Autowired MenuProcess menuProcess, @Autowired ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
		this.menuProcess = menuProcess;
		if (null == ComponentUtil.getCurrentUser()) {
			return;
		}
		H1 appTitle = new H1("MyApp");
		Image img = new Image("img/w5.png", "");//项目图标
		// 白色圆角底衬：logo 是蓝色图片，直接放在深蓝抽屉底上会看不清
		img.getStyle().set("margin", "10px 0 6px 16px").set("width", "45px").set("height", "45px")
				.set("background", "#ffffff").set("padding", "6px").set("border-radius", "10px");
		appTitle.getStyle().set("font-size", "var(--lumo-font-size-l)").set("line-height", "var(--lumo-size-l)")
				.set("margin", "0 var(--lumo-space-m)");

		VerticalLayout views = getPrimaryNavigation();//生成左侧栏

		DrawerToggle toggle = new DrawerToggle();//切换按钮
		projectBox = new ComboBox<Project>();
		projectBox.setWidth("200px");
		populateProjectCombobox(projectProcess);
		TabSheet subViews = getSecondaryNavigation();
		//添加头像
		Avatar namedAvatar = ComFactory.getNamedAvatar(ComponentUtil.getCurrentUserName(), "img/my.png");

		//添加用户名label
		Span label = ComFactory.getLabel(ComponentUtil.getCurrentUser().getUserName());

		Button standardButton = ComFactory.getTertriayBtn("退出");
		standardButton.addClickListener(e ->{
			getUI().ifPresent(ui -> ui.navigate(""));
			VaadinSession.getCurrent().getSession().invalidate();
		});

		// 一键关闭除「项目列表」外的所有标签页，避免标签页越开越多
		Button closeAllBtn = new Button(new Icon(VaadinIcon.CLOSE));
		closeAllBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
		closeAllBtn.setTooltipText("关闭全部标签页");
		closeAllBtn.getElement().setAttribute("aria-label", "关闭全部标签页");
		closeAllBtn.addClickListener(e -> {
			int closed = TabsUtil.closeAllTabs(mainTabsheet, PROJECT_LIST_TAB);
			if (closed > 0) {
				ComFactory.notificationSuccess("已关闭 " + closed + " 个标签页");
			} else {
				ComFactory.notification("没有可关闭的标签页");
			}
		});

		//顶部栏：左侧 = 抽屉开关 + 项目选择，右侧 = 头像 + 用户名 + 退出
		// 注意：主题组件 CSS（components/vaadin-horizontal-layout.css）给所有横向布局
		// 设了 justify-content: space-between + flex-wrap: wrap。原来左右组写死
		// 400px/300px 固定宽，右组内容（头像 + 用户名 + 铃铛 + 关闭全部 + 退出，
		// 约 340~370px）超过 300px 就被换行成两行：「退出」掉到第二行、溢出 48px
		// 顶栏被 Tab 条盖住点不到；wrapper 居中又把左组一起推低（项目下拉下移）。
		// 铃铛带未读徽章时更宽，所以普通用户必现、无徽章的管理员侥幸不复现。
		// 改法：自动宽度 + 不换行 + 显式 justify，从根上消除换行
		HorizontalLayout leftGroup = new HorizontalLayout(toggle, ComFactory.getLabel("项目："), projectBox);
		leftGroup.setAlignItems(Alignment.CENTER);
		leftGroup.setJustifyContentMode(JustifyContentMode.START);
		leftGroup.setSpacing(true);
		leftGroup.getStyle().set("flex-wrap", "nowrap");
		leftGroup.setFlexShrink(0);
		// 通知铃铛（T2-4）：未读数直接挂在按钮上，点开是通知列表弹窗
		NotificationBell bell = ComponentUtil.applicationContext.getBean(NotificationBell.class);

		HorizontalLayout rightGroup = new HorizontalLayout(namedAvatar, label, bell, closeAllBtn, standardButton);
		rightGroup.setAlignItems(Alignment.CENTER);
		rightGroup.setJustifyContentMode(JustifyContentMode.END);
		rightGroup.setSpacing(true);
		rightGroup.getStyle().set("flex-wrap", "nowrap");
		rightGroup.setFlexShrink(0);
		HorizontalLayout wrapper = new HorizontalLayout(leftGroup, rightGroup);
		wrapper.setWidthFull();
		wrapper.setJustifyContentMode(JustifyContentMode.BETWEEN);
		wrapper.setAlignItems(Alignment.CENTER);
		wrapper.setPadding(true);
		wrapper.setSpacing(true);
		wrapper.getStyle().set("padding", "0 var(--lumo-space-m)");
		wrapper.getStyle().set("flex-wrap", "nowrap");
		wrapper.getStyle().set("height", "48px");

		VerticalLayout viewHeader = new VerticalLayout(wrapper, subViews);
		viewHeader.setPadding(true);
		viewHeader.setSpacing(true);
		viewHeader.getStyle().set("background-color", "white");

		// 抽屉内容包一层白色圆角卡片：drawer 本体改浅灰底 + 内缩 padding，
		// 与浏览器左边缘留出间隙（样式见 styles.css 的 .app-drawer / .drawer-card）
		VerticalLayout drawerCard = new VerticalLayout(img, views);
		drawerCard.setSpacing(false);
		drawerCard.addClassName("drawer-card");
		addToDrawer(drawerCard);// 添加左侧抽屉栏
		addToNavbar(viewHeader);// 添加右侧主体区内容

		addClassName("app-drawer");//控制抽屉宽度等
		setPrimarySection(Section.DRAWER);
		MyProject pro = ComponentUtil.applicationContext.getBean(MyProject.class);
		TabsUtil.addComponentTab(mainTabsheet, PROJECT_LIST_TAB, "项目列表", pro);
	}

	/**
	 * 视图挂到 UI 上时，把主 TabSheet 绑定到当前 UI，供 TabsUtil 按会话取用。
	 */
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		MainTabsHolder.bind(attachEvent.getUI(), mainTabsheet);
	}

	/** 「项目列表」标签页的固定 key，切换项目时需要保留它 */
	public static final String PROJECT_LIST_TAB = "项目列表";

	/**
	 * 初始进入页面填充项目下拉框
	 * @param projectProcess
	 */
	private void populateProjectCombobox(ProjectProcess projectProcess) {
		List<Project> avilableProjects = projectProcess.getCurrentUserProjects();
//		List<String> collect = avilableProjects.stream().map( e -> e.getNameProject()).collect(Collectors.toList());
		projectBox.setItems(avilableProjects);
		List<ProjectUser> currentProjectOrer = projectProcess.getCurrentProjectOrer();
		if (null != currentProjectOrer && !currentProjectOrer.isEmpty()) {
			List<Project> collect = avilableProjects.stream()
					.filter(e -> e.getIdProject().equals(currentProjectOrer.get(0).getIdProject()))
					.collect(Collectors.toList());
			if (collect != null && !collect.isEmpty()) {
				projectBox.setValue(collect.get(0));
				VaadinSession.getCurrent().setAttribute("project", collect.get(0));
			} else if (avilableProjects.size() > 0) {
				projectBox.setValue(avilableProjects.get(0));
				VaadinSession.getCurrent().setAttribute("project", avilableProjects.get(0));
			}else {
				ComFactory.notification("当前未加入任务项目");
			}
		}
		projectBox.setItemLabelGenerator(Project :: getNameProject);
		projectBox.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<?>>() {
			private static final long serialVersionUID = -7038335787482214593L;

		@Override
		public void valueChanged(ValueChangeEvent<?> event) {
			Project value = (Project) event.getValue();
			if (null == value) {
				return;
			}
			VaadinSession.getCurrent().setAttribute("project", value);
			projectProcess.updateProjectUserTime(value.getIdProject());
			// 切换项目只刷新已打开视图的数据，不再清空标签页
			// （原实现 removeAllTabs 会把用户正在编辑的任务/文档一并丢掉）
			refreshOpenTabs();
			// 保证「项目列表」标签页始终存在
			if (!TabsUtil.checkTabExists(mainTabsheet, PROJECT_LIST_TAB)) {
				MyProject pro = ComponentUtil.applicationContext.getBean(MyProject.class);
				TabsUtil.addComponentTab(mainTabsheet, PROJECT_LIST_TAB, "项目列表", pro);
			}
		}

	/**
	 * 通知所有已打开且实现了 Refreshable 的视图重新取数。
	 */
	private void refreshOpenTabs() {
		for (Component content : TabsUtil.getTabContents(mainTabsheet)) {
			if (content instanceof Refreshable refreshable) {
				try {
					refreshable.refresh();
				} catch (Exception e) {
					log.error("切换项目后刷新视图失败，component={}", content.getClass().getSimpleName(), e);
					ComFactory.notificationError("部分页面刷新失败，请手动关闭后重新打开");
				}
			}
		}
	}
		});
	}

	/**
	 * 左侧导航：每个根菜单一个 AccordionPanel，直接放进普通 VerticalLayout。
	 * 不用 Accordion 容器——它的行为是同一时刻只允许一个面板展开（开新的自动收其它）；
	 * 独立的 AccordionPanel 各自独立开合，支持同时展开多个根菜单。
	 */
	private VerticalLayout getPrimaryNavigation() {
		VerticalLayout nav = new VerticalLayout();
		nav.setPadding(false);
		nav.setSpacing(false);
		// 全局 vaadin-vertical-layout.css 把所有竖向布局 hack 成 space-between，
		// 这里必须拉回 flex-start，否则面板会被均匀撑开、间隙乱掉
		nav.setJustifyContentMode(JustifyContentMode.START);
		nav.setAlignItems(Alignment.STRETCH);
		nav.addClassName("nav-accordion");//根菜单整体样式（沿用原选择器）
		List<Menu> selectAllMenusByUserId = menuProcess.selectAllMenusByUserId(Objects.requireNonNull(ComponentUtil.getCurrentUser()).getUserId());

		List<Menu> rootMenus = selectAllMenusByUserId.stream().filter(e -> e.getIdParent() == null).toList();
		for (Menu menu : rootMenus) {
			List<Menu> subMenus = selectAllMenusByUserId.stream().filter(e ->  e.getIdParent() != null && e.getIdParent().equals(menu.getIdMenu())).toList();
			ArrayList<Button> buttons = new ArrayList<>();
			for (Menu menu2 : subMenus) {
				buttons.add(createStyledAnchor(menu2));
			}
			AccordionPanel panel = new AccordionPanel();
			panel.setSummaryText(menu.getNameMenu());
			panel.add(createContent(buttons));
			// 常用的两个根菜单登录后默认展开，其余保持收起
			if ("项目管理".equals(menu.getNameMenu()) || "任务管理".equals(menu.getNameMenu()) || "我的待办".equals(menu.getNameMenu())) {
				panel.getElement().setProperty("opened", true);
			}
			nav.add(panel);
		}
		return nav;
	}
	/**
	 * 添加每个菜单按钮的功能
	 * @param menu2
	 * @return
	 */
	private Button createStyledAnchor(Menu menu2) {
		Button anchor = ComFactory.getTertriayBtn(menu2.getNameMenu());
		anchor.addClassName("nav-menu-btn");//全宽左对齐菜单项样式
		anchor.addClickListener(e ->{
			Component createComponent = ComponentUtil.createComponent(menu2);
			if (null != createComponent) {
				// key 用菜单 id：重复点击同一个菜单只激活已有标签页，不会无限新增
				TabsUtil.addComponentTab(mainTabsheet, "menu-" + menu2.getIdMenu(), menu2.getNameMenu(), createComponent);
			}
		});
			
//		RquestComponent.addTab(tabs, text));
		return anchor;
	}

	private TabSheet getSecondaryNavigation() {
		mainTabsheet = ComFactory.getTabSheet();
		mainTabsheet.addClassName("main-tabsheet");//tabs现代化样式
		mainTabsheet.getStyle().set("margin-top", "0");
		return mainTabsheet;
	}

	private VerticalLayout createContent(List<Button> buttons) {
		VerticalLayout content = new VerticalLayout();
		content.setPadding(false);
		content.setSpacing(false);
		content.setAlignItems(Alignment.STRETCH);
		buttons.forEach(e -> content.add(e));
		return content;
	}

	public TabSheet getMainTabsheet() {
		return mainTabsheet;
	}

	public void setMainTabsheet(TabSheet mainTabsheet) {
		this.mainTabsheet = mainTabsheet;
	}

	
}
