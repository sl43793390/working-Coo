package com.sl.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.entity.Project;
import com.sl.entity.ProjectTaskFile;
import com.sl.service.ProjectProcess;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.FailedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 项目文档页面，该页面可以查看当前项目的所有文档，和项目包含的作业的所有文档，并支持上传文件功能
 * 
 * @author Administrator
 *
 */
@SpringComponent
@Scope("prototype")
public class ProjectDocumentComponent extends VerticalLayout implements ComponentEventListener<FailedEvent>, Refreshable {

	private static final long serialVersionUID = 1009297120399909557L;

	private static final Logger log = LoggerFactory.getLogger(ProjectDocumentComponent.class);

	private String parentPath;

	private File file;

	private ProjectProcess projectProcess;

	private Grid<ProjectTaskFile> docGrid;
	/** 列表空态提示 */
	private Span emptyHint;
	private RichTextEditor richTextEditor;

	private Button searchBtn;

	private TextField fileName;

	public ProjectDocumentComponent(@Autowired ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
		add(ComFactory.getTitle("项目文档"));
		add(ComFactory.getHorizontalLine());
		initFilterLayout();// 筛选区
		initFileListTable();// 文档列表展示
		initFileUploadLayout();// 上传区
		registerHandler();
	}

	/**
	 * 筛选区：只留文件名搜索。项目维度统一走顶栏的项目下拉
	 * （ComponentUtil.getCurrentProject()），页面内不再重复放项目选择。
	 */
	private void initFilterLayout() {
		HorizontalLayout layoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		layoutLeft.setHeight("40px");
		add(layoutLeft);

		Span nameLb2 = ComFactory.getLabel("文件名称：");
		fileName = ComFactory.getTextField();
		searchBtn = ComFactory.getPrimaryBtn("搜索");
		layoutLeft.add(nameLb2, fileName, searchBtn);
	}

	private void initFileListTable() {
		docGrid = ComFactory.getGrid(ProjectTaskFile.class);
		docGrid.setHeight("300px");
		docGrid.setWidth("98%");
		docGrid.setSelectionMode(SelectionMode.SINGLE);
		docGrid.addColumn(ProjectTaskFile::getNameFile).setHeader("文件名称");
		// 类型徽章：取扩展名，一眼分辨文档/图片/压缩包
		docGrid.addComponentColumn(p -> {
			String ext = fileExt(p.getNameFile());
			Span badge = new Span(ext.isEmpty() ? "文件" : ext.toUpperCase());
			badge.setClassName("doc-type-badge");
			return badge;
		}).setHeader("类型").setAutoWidth(true).setFlexGrow(0);
		docGrid.addColumn(e -> DateUtil.formatDate(e.getDtUpload())).setHeader("上传时间");
		docGrid.addColumn(ProjectTaskFile::getIdUser).setHeader("上传用户");
		// S6 预览：图片内嵌显示，PDF 内嵌 viewer
		docGrid.addComponentColumn(p -> {
			Button previewBtn = ComFactory.getPrimaryBtn("预览");
			previewBtn.setVisible(isPreviewable(p.getNameFile()));
			previewBtn.addClickListener(e -> openPreview(p));
			return previewBtn;
		}).setHeader("预览").setAutoWidth(true).setFlexGrow(0);
		docGrid.addComponentColumn(p -> {
//			Button standardButton = ComFactory.getStandardButton("下载");
//			standardButton.setIcon(new Icon(VaadinIcon.DOWNLOAD));
//			standardButton.addClickListener(e -> HttpUtil.get(""));
			Anchor an = new Anchor(ComFactory.buildFileUrl("/file/download?fileId=" + p.getIdFile()), "下载", AnchorTarget.BLANK);
			return an;
		}).setHeader("下载");

		reloadFileList();
		add(docGrid);
		emptyHint = ComFactory.getEmptyHint("当前项目下暂无文档，可在下方「上传文件」添加");
		add(emptyHint);
	}

	/**
	 * 按当前项目重新加载文档列表。切换项目和搜索后都会调用。
	 */
	private void reloadFileList() {
		Project current = ComponentUtil.getCurrentProject();
		if (null == current) {
			docGrid.setItems(new ArrayList<ProjectTaskFile>());
			ComFactory.toggleEmptyHint(emptyHint, 0);
			return;
		}
		List<ProjectTaskFile> files = projectProcess.getProjectFileListByIdProject(current.getIdProject());
		docGrid.setItems(files);
		ComFactory.toggleEmptyHint(emptyHint, files.size());
	}

	/**
	 * 顶栏切换项目时被调用：重新加载文档列表。
	 */
	@Override
	public void refresh() {
		reloadFileList();
	}

	/**
	 * 上传布局
	 */
	private void initFileUploadLayout() {
		if (null == ComponentUtil.getCurrentProject()) {
			ComFactory.notificationError("当前还未创建项目，请先创建项目");
			return;
		}
		// 加载配置文件中配置的路径，在此路径下再拼接用户+项目ID
		parentPath = ComponentUtil.getCurrentUserFilePath();
		// 上传成功将需要的信息保存到数据库（新 API：成功回调直接拿到保存好的文件）
		Upload upload2 = ComFactory.getUpload(parentPath, f -> {
			ProjectTaskFile pfile = new ProjectTaskFile();
			pfile.setIdFile(IdUtil.getSnowflakeNextIdStr());
			pfile.setIdProject(ComponentUtil.getCurrentProject().getIdProject());
			pfile.setDtUpload(new Date());
			pfile.setNameFile(f.getName());
			pfile.setCdFilePath(parentPath);
			pfile.setIdUser(ComponentUtil.getCurrentUser().getUserId());
			pfile.setCdComment(richTextEditor.asHtml().getValue());
			projectProcess.saveFile(pfile);
			ComFactory.notificationSuccess("上传成功");
		});
		add(upload2);
		add(ComFactory.getLabel("文件说明："));
		richTextEditor = ComFactory.getRichTextArea();
		richTextEditor.setWidth("98%");
		richTextEditor.setHeight("200px");
		add(richTextEditor);
	}

	public void registerHandler() {
//		根据当前项目 + 文件名模糊搜索
		searchBtn.addClickListener(e -> {
			Project current = ComponentUtil.getCurrentProject();
			if (null == current) {
				ComFactory.notification("请先在顶部选择项目");
				return;
			}
			List<ProjectTaskFile> res = projectProcess.getProjectFileListByIdProject(current.getIdProject());
			if (fileName.getValue() != null && StrUtil.isBlankIfStr(fileName.getValue())) {
				List<ProjectTaskFile> collect = res.stream().filter(e1 -> e1.getNameFile().contains(fileName.getValue().toString())).collect(Collectors.toList());
				docGrid.setItems(collect);
				ComFactory.toggleEmptyHint(emptyHint, collect.size());
			}else {
				docGrid.setItems(res);
				ComFactory.toggleEmptyHint(emptyHint, res.size());
			}
		});
		// 文件名输入框回车直接搜索，不用非得点按钮
		fileName.addKeyPressListener(Key.ENTER, e -> searchBtn.click());
		// 点击显示文本框中的文件说明
		docGrid.addItemClickListener(item -> {
			Set<ProjectTaskFile> selectedItems = docGrid.getSelectedItems();
			if (null == selectedItems || selectedItems.isEmpty()) {
				return;//再次点击已选中行会先取消选中，此时不更新说明
			}
			String comment = selectedItems.stream().findFirst().get().getCdComment();
			richTextEditor.asHtml().setValue(StrUtil.nullToEmpty(comment));
		});
	}

//	@Override
//	public OutputStream receiveUpload(String filename, String mimeType) {
//		String keypath;
//		if (null != parentPath) {
//			FileUtil.mkdir(parentPath);
//			keypath = parentPath+ File.separator + filename;
//		}else {//如果没有设置文件存放目录则在当前程序运行目录下存放
//			String userdir = System.getProperty("user.dir");
//			keypath = userdir + File.separator + filename;
//		}
//		log.info("上传文件路径为："+keypath);
//		file = new File(keypath);
//		FileOutputStream fileStream = null;
//		try {
//			fileStream = new FileOutputStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return fileStream;
//	}

	@Override
	public void onComponentEvent(FailedEvent event) {
		Notification.show("上传文件失败", 5000, Notification.Position.MIDDLE);
	}

	/** 取文件扩展名（不含点、小写），无扩展名返回空串 */
	private static String fileExt(String name) {
		if (null == name) {
			return "";
		}
		int idx = name.lastIndexOf('.');
		return idx < 0 ? "" : name.substring(idx + 1).toLowerCase();
	}

	// ==================== S6 附件预览（图片 / PDF） ====================

	private static final java.util.Set<String> PREVIEW_IMAGE_EXTS = java.util.Set.of(
			"png", "jpg", "jpeg", "gif", "webp", "bmp", "svg");

	private static boolean isPreviewable(String name) {
		String ext = fileExt(name);
		return PREVIEW_IMAGE_EXTS.contains(ext) || "pdf".equals(ext);
	}

	/** 预览弹窗：图片用 <img>，PDF 用内嵌 iframe，走 /file/preview（inline，鉴权同下载） */
	private void openPreview(ProjectTaskFile f) {
		com.vaadin.flow.component.dialog.Dialog dlg = new com.vaadin.flow.component.dialog.Dialog();
		dlg.setHeaderTitle(f.getNameFile());
		dlg.setWidth("860px");
		dlg.setHeight("640px");
		dlg.setCloseOnOutsideClick(true);
		if ("pdf".equals(fileExt(f.getNameFile()))) {
			com.vaadin.flow.component.html.IFrame frame =
					new com.vaadin.flow.component.html.IFrame(
							ComFactory.buildFileUrl("/file/preview?fileId=" + f.getIdFile()));
			frame.setSizeFull();
			dlg.add(frame);
		} else {
			com.vaadin.flow.component.html.Image img =
					new com.vaadin.flow.component.html.Image(
							ComFactory.buildFileUrl("/file/preview?fileId=" + f.getIdFile()), f.getNameFile());
			img.setMaxWidth("100%");
			img.setMaxHeight("100%");
			VerticalLayout wrap = new VerticalLayout(img);
			wrap.setAlignItems(Alignment.CENTER);
			wrap.setSizeFull();
			dlg.add(wrap);
		}
		Button close = ComFactory.getPrimaryBtn("关闭");
		close.addClickListener(e -> dlg.close());
		dlg.getFooter().add(close);
		dlg.open();
	}

}