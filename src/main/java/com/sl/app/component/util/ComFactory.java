package com.sl.app.component.util;

import java.io.File;
import java.time.LocalDate;

import com.vaadin.componentfactory.TreeComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.CancelEvent;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.function.ValueProvider;

public class ComFactory {

	/**
	 * 构造文件接口（下载 /file/download、预览 /file/preview）的带 context-path 的 URL（S6）。
	 * 原先各页面写死 "/file/download?fileId=..."，而 server.servlet.context-path=/work，
	 * 浏览器按根上下文解析会 404。这里统一拼上当前请求的 context path；
	 * 拿不到当前请求（理论上不会发生，组件都在 Vaadin 请求内构建）时退回原行为。
	 *
	 * @param pathAndQuery 如 "/file/download?fileId=xxx"
	 */
	public static String buildFileUrl(String pathAndQuery) {
		String ctx = "";
		try {
			com.vaadin.flow.server.VaadinRequest request = com.vaadin.flow.server.VaadinService.getCurrentRequest();
			if (null != request) {
				ctx = request.getContextPath();
			}
		} catch (Exception ignore) {
			// 非 Vaadin 请求上下文，退回原行为
		}
		return (null == ctx ? "" : ctx) + pathAndQuery;
	}

	/**
	 * 创建一个水平布局layout ，该layout放入多个组合默认靠左侧依顺序放置
	 * 注意：水平布局的layout，内部组件需要设置宽度，否则每个就会占据所有宽度，效果和verticalLayout一样了。
	 * @return
	 */
	public static HorizontalLayout  getResponsiveHorizontalLayoutLeft(){
		HorizontalLayout layout = new HorizontalLayout();
		layout.setJustifyContentMode(JustifyContentMode.START);
		layout.addClassName("list-view-horizontalLayout");
		layout.setWidth("100%");
		return layout;
	}
	/**
	 * 创建一个水平布局layout ，该layout放入多个组合默认靠右侧侧依顺序放置
	 * 注意：水平布局的layout，内部组件需要设置宽度，否则每个就会占据所有宽度，效果和verticalLayout一样了。
	 * @return
	 */
	public static HorizontalLayout  getResponsiveHorizontalLayoutRight(){
		HorizontalLayout layout = new HorizontalLayout();
		layout.setJustifyContentMode(JustifyContentMode.END);
		layout.addClassName("list-view-horizontalLayout");
		layout.setWidth("100%");
		return layout;
	}
	/**
	 * 创建一个水平布局layout ，该layout放入多个组合默认居中侧侧依顺序放置
	 * 注意：水平布局的layout，内部组件需要设置宽度，否则每个就会占据所有宽度，效果和verticalLayout一样了。
	 * @return
	 */
	public static HorizontalLayout  getResponsiveHorizontalLayoutMiddle(){
		HorizontalLayout layout = new HorizontalLayout();
		layout.setJustifyContentMode(JustifyContentMode.CENTER);
		layout.addClassName("list-view-horizontalLayout");
		layout.setWidth("100%");
		return layout;
	}
	
	public static FormLayout getFormLayout() {
		FormLayout form = new FormLayout();
		return form;
	}
	/**
	 * 创建formlayout并加入component
	 * @param com
	 * @return
	 */
	public static FormLayout getFormLayout(Component... com) {
		FormLayout form = new FormLayout(com);
		return form;
	}
	
	public static VerticalLayout getVerticalLayout() {
		VerticalLayout form = new VerticalLayout();
		form.setAlignItems(Alignment.START);
		return form;
	}
	
	public static VerticalLayout getVerticalLayout(Alignment align) {
		VerticalLayout form = new VerticalLayout();
		form.setAlignItems(align);
		return form;
	}
	
	public static Button getPrimaryBtn() {
		Button btn = new Button();
		btn.addClassName("vaadin-button-back");
		btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return btn;
	}
	
	public static Button getPrimaryBtn(VaadinIcon icon) {
		Button btn = new Button();
		btn.setIcon(new Icon(icon));
		btn.addClassName("vaadin-button-back");
		btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return btn;
	}
	
	public static Button getPrimaryBtn(String caption) {
		Button btn = new Button(caption);
		btn.addClassName("vaadin-button-back");
		btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return btn;
	}
	public static Button getPrimaryBtn(String caption,String width) {
		Button btn = new Button(caption);
		btn.setWidth(width);
		btn.addClassName("vaadin-button-back");
		btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return btn;
	}
	/**
	 * 带图标的主按钮。
	 */
	public static Button getPrimaryBtn(String caption,VaadinIcon icon) {
		Button btn = new Button(caption);
		btn.setIcon(new Icon(icon));
		btn.addClassName("vaadin-button-back");
		btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return btn;
	}
	
	public static NativeButton getNativeButton(String caption) {
		NativeButton btn = new NativeButton(caption);
		return btn;
	}
	
	public static Button getTertriayBtn(String caption) {
		Button btn = new Button(caption);
		btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		return btn;
	}
	
	public static Button getTertriayBtn(String caption,VaadinIcon icon) {
		Button btn = new Button(caption);
		btn.setIcon(new Icon(icon));
		btn.addClassName("vaadin-button-back");
		btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		return btn;
	}
	/**
	 * 白底蓝字
	 * @param caption
	 * @return
	 */
	public static Button getSmallBtn(String caption) {
		Button smallButton = new Button(caption);
		smallButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
		return smallButton;
	}
	/**
	 * 白底蓝字
	 * @param caption
	 * @return
	 */
	public static Button getLargeBtn(String caption) {
		Button smallButton = new Button(caption);
		smallButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
		return smallButton;
	}
	
	public static Checkbox getCheckbox(String caption) {
		Checkbox ck = new Checkbox(caption);
		return ck;
	}
	
	public static HorizontalLayout getTitle(String title) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setHeight("40px");
		Span lb = new Span(title);
		lb.addClassName("lb-title");
		layout.add(lb);
		return layout;
	}
	
	public static Span getTitleLabel(String title) {
		Span lb = new Span(title);
		lb.addClassName("lb-title");
		return lb;
	}
	public static Span getTitleLabel() {
		Span lb = new Span();
		lb.addClassName("lb-title");
		return lb;
	}
	
	public static Span getHorizontalLine() {
		Span lb = new Span();
		lb.getStyle().set("height", "1px").set("width", "90%").set("background-color", "gray");
		return lb;
	}
	
	public static Span getHorizontalLine(String widthPercent) {
		Span lb = new Span();
		lb.getStyle().set("height", "1px").set("width", widthPercent).set("background-color", "gray");
		return lb;
	}
	
	public static Span getLabel(String text) {
		Span lb = new Span(text);
		lb.setHeight("40px");
		lb.addClassName("lb-width-common");
		return lb;
	}
	
	public static Span getLabel() {
		Span lb = new Span();
		lb.setHeight("40px");
		lb.addClassName("lb-width-common");
		return lb;
	}
	
	public static Span getLabel(String text,String width) {
		Span lb = new Span(text);
		lb.setHeight("40px");
		lb.setWidth(width);
		lb.addClassName("lb-width-common");
		return lb;
	}
	
	/* ==================================================================
	 * 表单字段级校验
	 * 原先所有校验都堆在「保存」按钮里用 try-catch + Notification 兜底，
	 * 用户必须点一次保存才知道哪里填错了。这里改成输入即校验，错误直接挂在字段上。
	 * ================================================================== */

	/**
	 * 必填校验，失焦或输入时即时提示。
	 */
	public static void bindRequired(HasValidation field, HasValue<?, ? extends Object> source, String message) {
		source.addValueChangeListener(e -> validateRequired(field, source, message));
	}

	public static void bindRequired(TextField field, String message) {
		field.setRequiredIndicatorVisible(true);
		bindRequired(field, field, message);
	}

	private static void validateRequired(HasValidation field, HasValue<?, ?> source, String message) {
		Object v = source.getValue();
		boolean invalid = (null == v) || v.toString().trim().isEmpty();
		field.setInvalid(invalid);
		field.setErrorMessage(message);
	}

	/**
	 * 数值区间校验，空值视为合法（表示不填）。
	 */
	public static void bindNumberRange(TextField field, double min, double max, String message) {
		field.addValueChangeListener(e -> {
			String v = field.getValue();
			boolean invalid = false;
			if (null != v && !v.trim().isEmpty()) {
				try {
					double d = Double.parseDouble(v.trim());
					invalid = d < min || d > max;
				} catch (NumberFormatException ex) {
					invalid = true;
				}
			}
			field.setInvalid(invalid);
			field.setErrorMessage(message);
		});
	}

	/**
	 * 保存前统一检查：只要有一个字段处于错误态就返回 false（错误已显示在字段上）。
	 */
	public static boolean validateFields(HasValidation... fields) {
		boolean ok = true;
		for (HasValidation f : fields) {
			if (null != f && f.isInvalid()) {
				ok = false;
			}
		}
		return ok;
	}

	/**
	 * 列表空态提示。Vaadin 的 Grid 没有内置空态，
	 * 原先列表没数据时页面只剩一个空表格，用户分不清是「没数据」还是「加载失败」。
	 * 用法：把它加在 Grid 附近，根据数据条数切换 setVisible。
	 */
	public static Span getEmptyHint(String text) {
		Span hint = new Span(text);
		hint.addClassName("empty-hint");
		hint.setVisible(false);
		return hint;
	}

	/**
	 * 按数据条数切换空态提示的显示。
	 */
	public static void toggleEmptyHint(Span emptyHint, long count) {
		if (null != emptyHint) {
			emptyHint.setVisible(count <= 0);
		}
	}

	public static Span getBlankLabel(String width) {
		Span lb = new Span();
		lb.setHeight("40px");
		lb.setWidth(width);
		return lb;
	}
	
	public static TabSheet getTabSheet() {
		TabSheet tabs = new TabSheet();
		tabs.setWidthFull();
		return tabs;
	}
	public static void addTab(TabSheet tabs,String text){
		Tab t = new Tab(new TabHeader(tabs, text));
		tabs.add(t,new Span(text));
		tabs.setSelectedTab(t);
	}
	public static void addComponentTab(TabSheet tabs,String text,Component com){
		Tab t = new Tab(new TabHeader(tabs, text));
		tabs.add(t,com);
		tabs.setSelectedTab(t);
	}
	public static void addComponentTab(TabSheet tabs,String text,Component com,boolean closeable){
		Tab t = new Tab(new TabHeader(tabs, text,closeable));
		tabs.add(t,com);
		tabs.setSelectedTab(t);
	}
	
	public static <T> Grid<T> getGrid(Class<T> clazz) {
		Grid<T> grid = new Grid<T>(clazz);
		grid.removeAllColumns();
		return grid;
	}
	
	public static <T> TreeGrid<T> getTreeGrid(Class<T> clazz,SelectionMode mode) {
		TreeGrid<T> grid = new TreeGrid<T>(clazz);
		grid.removeAllColumns();
		grid.setSelectionMode(mode);
		return grid;
	}
	/**
	 * 
	 * @param <T>因为grid 默认展示泛型对象的所不是null的列，一般使用都是指定展示那些列，所以从该处默认删除了所有默认的列；
	 * 若需要保留则调用另一个方法@getGridProWithAllColumn，若需要保留部分列可调用componentUtil里的方法@filterGridColumns
	 * @param clazz 存放类型
	 * @param editable 是否可编辑
	 * @return
	 */
	public static <T> GridPro<T> getGridPro(Class<T> clazz,boolean editable) {
		GridPro<T> grid = new GridPro<T>(clazz);
		grid.setEditOnClick(editable);//设置可编辑
		grid.removeAllColumns();
		return grid;
	}
	/**
	 * 保留默认泛型对象中的所有列，不做删除，需要删除后的grid 见另一个方法@getgridPro
	 * @param <T>
	 * @param clazz
	 * @param editable
	 * @return
	 */
	public static <T> GridPro<T> getGridProWithAllColumn(Class<T> clazz,boolean editable) {
		GridPro<T> grid = new GridPro<T>(clazz);
		grid.setEditOnClick(editable);//设置可编辑
		return grid;
	}
	
	public static <T> Crud<T> getCrud(Class<T> clazz,CrudEditor<T> editor) {
		Crud<T> crud = new Crud<T>(clazz,editor);
		return crud;
	}
	
	public static <T> Crud<T> getCrud(Class<T> clazz,CrudEditor<T> editor,AbstractDataProvider<T, ?> dataProvider) {
		Crud<T> crud = new Crud<T>(clazz,editor);
		crud.setDataProvider(dataProvider);
		return crud;
	}
	
	public static TextField getTextFieldWithClearBtn(String caption) {
		TextField com1 = new TextField(caption);
		com1.setClearButtonVisible(true);
		return com1;
	}
	public static TextField getTextField(String caption) {
		TextField com1 = new TextField(caption);
		return com1;
	}
	
	public static PasswordField getPasswordField() {
		PasswordField field = new PasswordField();
		return field;
	}
	public static PasswordField getPasswordField(String caption) {
		PasswordField field = new PasswordField(caption);
		return field;
	}
	
	public static TextField getTextField() {
		TextField com1 = new TextField();
		return com1;
	}
	
	public static ComboBox<String> getComboboxWithItems(String caption,String... items){
		ComboBox<String> com2 = new ComboBox<>(caption);
		com2.setItems(items);
		return com2;
	}
	public static ComboBox<String> getCombobox(String caption){
		ComboBox<String> com2 = new ComboBox<>(caption);
		return com2;
	}
	public static ComboBox<String> getCombobox(){
		ComboBox<String> com2 = new ComboBox<>();
		return com2;
	}
	public static MultiSelectComboBox<String> getComboboxMultiSelect(){
		MultiSelectComboBox<String> com2 = new MultiSelectComboBox<>();
		return com2;
	}
	public static MultiSelectComboBox<String> getComboboxMultiSelect(String caption){
		MultiSelectComboBox<String> com2 = new MultiSelectComboBox<>(caption);
		return com2;
	}
	
	public static <T> MultiSelectComboBox<T> getComboboxMultiSelect(Class<T> clazz){
		MultiSelectComboBox<T> com2 = new MultiSelectComboBox<T>();
		return com2;
	}
	public static <T> MultiSelectComboBox<T> getComboboxMultiSelect(String caption,Class<T> clazz){
		MultiSelectComboBox<T> com2 = new MultiSelectComboBox<T>(caption);
		return com2;
	}
	
	public static MultiSelectListBox<String> getListBox(String... items) {
		MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
        listBox.setItems(items);
        return listBox;
	}
	
	public static MultiSelectListBox<String> getListBox() {
		MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
		return listBox;
	}
	
	public static <T> ComboBox<T> getCombobox(Class<T> clazz){
		ComboBox<T> com2 = new ComboBox<T>();
		return com2;
	}
	/**
	 * 树结构的下拉
	 * @param <T>
	 * @param treeData
	 * @param valueProvider
	 * @return
	 */
	public static <T> TreeComboBox<T> getTreeCombobox(TreeData<T> treeData, ValueProvider<T, String> valueProvider){
		TreeComboBox<T> com = new TreeComboBox<T>(treeData,valueProvider);
		return com;
	}
	
	public static TextArea getTextArea() {
		TextArea area = new TextArea();
		area.setWidth("100%");
		return area;
	}
	public static TextArea getTextArea(String caption) {
		TextArea area = new TextArea(caption);
		area.setWidthFull();
		return area;
	}
	//vaadin自带的富文本编辑器没有自动换行功能
	public static RichTextEditor getRichTextEditor(String width,String height) {
		RichTextEditor editor = new RichTextEditor();
		editor.getStyle().set("border", "solid 1px #dadada");
		editor.setHeight(height);
		editor.setWidth(width);
		return editor;
	}
	
	public static RichTextEditor getRichTextEditor() {
		RichTextEditor editor = new RichTextEditor();
		editor.getStyle().set("border", "solid 1px #dadada");
		editor.setWidthFull();
		return editor;
	}
	
	public static RichTextEditor getRichTextArea(String width,String height) {
		return getRichTextEditor(width, height);
	}
	
	public static RichTextEditor getRichTextArea() {
		return getRichTextEditor();
	}
	/**
	 * 可传入保存取消按钮的对话框
	 * @param title
	 * @param saveButton
	 * @param cancelButton
	 * @return
	 */
	public static Dialog getDialog(String title,Button saveButton,Button cancelButton) {
		Dialog dialog = new Dialog();
		dialog.setCloseOnOutsideClick(false);
		dialog.setHeaderTitle(title);
		dialog.getFooter().add(saveButton);
		dialog.getFooter().add(cancelButton);
		return dialog;
	}
	/**
	 * 
	 * @param title 标题
	 * @param layout 内容布局
	 * @param saveButton 保存
	 * @param cancelButton 取消
	 * @return
	 */
	public static Dialog getDialog(String title,Component layout,Button saveButton,Button cancelButton) {
		Dialog dialog = new Dialog();
		dialog.setCloseOnOutsideClick(false);
		dialog.setHeaderTitle(title);
		dialog.add(layout);
		dialog.getFooter().add(saveButton);
		dialog.getFooter().add(cancelButton);
		return dialog;
	}
	
	/**
	 * 确认对话框,传入确认、取消事件实现即可
	 * @return 
	 */
	public static ConfirmDialog getConfirmationDialog(ComponentEventListener<ConfirmEvent> confirmListener,ComponentEventListener<CancelEvent> cancelListener) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("提示");
        dialog.setText("请确认是否要删除所选内容？");

        dialog.setConfirmText("确认");
        dialog.setConfirmButton("确认", confirmListener);
        dialog.setCancelable(true);
        dialog.setCancelButton("取消", cancelListener);
        
        return dialog;
	}
	public static ConfirmDialog getConfirmationDialog(String prompt,ComponentEventListener<ConfirmEvent> confirmListener,ComponentEventListener<CancelEvent> cancelListener) {
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setHeader("提示");
		dialog.setText(prompt);
		
		dialog.setConfirmText("确认");
		dialog.setConfirmButton("确认", confirmListener);
		dialog.setCancelable(true);
		dialog.setCancelButton("取消", cancelListener);
		
		return dialog;
	}
	
	public static Dialog getDialog(String title) {
		Dialog dialog = new Dialog();
		dialog.setCloseOnOutsideClick(false);
		dialog.setHeaderTitle(title);
		return dialog;
	}
	
	public static Avatar getBasicAvatar() {
		 Avatar avatarBasic = new Avatar();
		return avatarBasic;
	}
	public static Avatar getNamedAvatar(String name) {
        Avatar avatarName = new Avatar(name);
		return avatarName;
	}
	
	public static Avatar getNamedAvatar(String name,String pictureUrl) {
		Avatar avatarImage = new Avatar(name);
        avatarImage.setImage(pictureUrl);
        avatarImage.setTooltipEnabled(true);
		return avatarImage;
	}
	
	public static AvatarGroup getAvatarGroup() {
		 AvatarGroup avatarGroup = new AvatarGroup();
		return avatarGroup;

//	        for (Person person : people) {
//	            String name = person.getFirstName() + " " + person.getLastName();
//	            AvatarGroupItem avatar = new AvatarGroupItem(name);
//	            avatar.setColorIndex(colorIndex++);
//	            avatarGroup.add(avatar);
//	        }
	}
	/**
	 * 一个绿色对钩
	 * @return
	 */
	public static Icon getConfirmIcon() {
		Icon confirmed = createIcon(VaadinIcon.CHECK, "Confirmed");
		confirmed.getElement().getThemeList().add("badge success");
		return confirmed;
	}
	/**
	 * 一个红色的叉叉
	 * @return
	 */
	public static Icon getCancelIcon() {
		Icon cancelled = createIcon(VaadinIcon.CLOSE_SMALL, "Cancelled");
		cancelled.getElement().getThemeList().add("badge error");
		return cancelled;
	}
	
	private static Icon createIcon(VaadinIcon vaadinIcon, String label) {
	    Icon icon = vaadinIcon.create();
	    icon.getStyle().set("padding", "var(--lumo-space-xs");
	    // Accessible label
	    icon.getElement().setAttribute("aria-label", label);
	    // Tooltip
	    icon.getElement().setAttribute("title", label);
	    return icon;
	}
	/**
	 * 一个蓝色字体的标签，浅蓝色底色，看着像button。
	 * @param caption
	 * @return
	 */
	public static Span getNormalBadge(String caption) {
		Span pending = new Span(caption);
		pending.getElement().getThemeList().add("badge");
		return pending;
	}
	/**
	 * 一个绿色字体的标签，浅绿色底色，看着像button。
	 * @param caption
	 * @return
	 */
	public static Span getConfirmBadge(String caption) {
		Span confirmed = new Span(caption);
		confirmed.getElement().getThemeList().add("badge success");
		return confirmed;
	}
	/**
	 * 一个红色字体的标签，浅红色底色，看着像button。
	 * @param caption
	 * @return
	 */
	public static Span getErrorBadge(String caption) {
		Span denied = new Span(caption);
		denied.getElement().getThemeList().add("badge error");
		return denied;
	}
	/**
	 * 一个灰色字体的标签，浅灰色底色，看着像button。
	 * @param caption
	 * @return
	 */
	public static Span getGrayBadge(String caption) {
		Span onHold = new Span(caption);
		onHold.getElement().getThemeList().add("badge contrast");
		return onHold;
	}
	
	/**
	 * board 是一个响应式的布局，一行有四列，缩小会自动换行
	 * @return
	 */
	public static Board getBoard() {
		Board board = new Board();
		return board;
	}
	
	public static DatePicker getDateField(String caption,LocalDate initialDate) {
		DatePicker datep = new DatePicker(caption);
		datep.setValue(initialDate);
		return datep;
	}
	public static DatePicker getDateField(String caption,LocalDate initialDate,String width) {
		DatePicker datep = new DatePicker(caption);
		datep.setWidth(width);
		datep.setValue(initialDate);
		return datep;
	}
	
	public static DatePicker getDatePicker(String caption,LocalDate initialDate) {
		return getDateField(caption, initialDate);
	}
	public static DatePicker getDatePicker(String caption) {
		DatePicker datep = new DatePicker(caption);
		return datep;
	}
	
	public static Board getBoardWithFourColumn(Component com1,Component com2,Component com3,Component com4) {
		Board board = new Board();
		Row row1 = new Row();// 一行放四个列
		row1.add(com1,com2,com3,com4);
		return board;
	}
	
	public static void addBoardRow(Board bo,Component com1,Component com2,Component com3,Component com4) {
		Row row = new Row();
		row.add(com1,com2,com3,com4);
		bo.add(row);
	}
	
	/**
	 * 用户自己写拒绝回调函数：传入保存目录
	 * //失败添加监听器
	        upload.addFileRejectedListener(event -> {
	            String errorMessage = event.getErrorMessage();
	            Notification notification = Notification.show(errorMessage, 5000, Notification.Position.MIDDLE);
	            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
	        });
	 * @param uploadDir
	 * @return
	 */
	/**
	 * 上传组件（Vaadin 24.8+ 新 API：UploadHandler 直接落盘，
	 * 替代已废弃的 Receiver / MultiFileBuffer / FileReceiverImpl 方案）。
	 * 文件写入 uploadDir 目录（保留客户端原始文件名，目录不存在会自动创建），
	 * 每个文件保存成功后回调 onSaved（参数为保存好的 File）。
	 * 回调内部经 ui.access 包裹：无论服务端在哪个线程执行回调，都可以安全地
	 * 操作组件、发通知、写数据库。
	 * @param uploadDir 保存目录
	 * @param onSaved   单个文件成功后的回调，可为 null
	 */
	public static Upload getUpload(String uploadDir, java.util.function.Consumer<File> onSaved) {
		UI ui = UI.getCurrent();
		Upload upload = new Upload(com.vaadin.flow.server.streams.UploadHandler.toFile((metadata, file) -> {
			if (null != ui) {
				ui.access(() -> {
					Notification.show("保存成功", 5000, Notification.Position.MIDDLE);
					if (null != onSaved) {
						onSaved.accept(file);
					}
				});
			} else if (null != onSaved) {
				onSaved.accept(file);
			}
		}, metadata -> {
			File dir = new File(uploadDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return new File(dir, metadata.fileName());
		}));
	    upload.setAutoUpload(true); //自动开始上传
	    UploadExamplesI18N i18n = new UploadExamplesI18N();
        upload.setI18n(i18n);
        upload.setMaxFiles(1);//默认只能同时上传一个文件
        //限制文件类型
//        upload.setAcceptedFileTypes("application/pdf", ".pdf",".jpg",".png","docx","xls","xlsx","zip");
//        int maxFileSizeInBytes = 100* 1024 * 1024; // 100MB/注意此处设置不一定起作用，spring本身会有大小拦截，需要配置在application.properties中
//        upload.setMaxFileSize(maxFileSizeInBytes);
        upload.addFileRejectedListener(e -> Notification.show("上传文件大小或类型不符合要求，请重新上传", 5000, Notification.Position.MIDDLE));
        return upload;
	}
	/**
	 * create an enpty upload without listener（新 API，不挂成功回调）
	 * @param uploadDir
	 * @return
	 */
	public static Upload getUploadEmpty(String uploadDir) {
		return getUpload(uploadDir, null);
	}
	
	public static ProgressBar getProgressBar() {
		ProgressBar progressBar = new ProgressBar();
		return progressBar;
	}
	
	public static Chart getChart(ChartType charttype) {
		Chart chart = new Chart(charttype);
		return chart;
	}
	public static TextField getStandardTextField(String caption) {
		return getTextField(caption);
	}

	public static TextField getStandardTextField(String caption,String width) {
		TextField textField = getTextField(caption);
		textField.setWidth(width);
		return textField;
	}

	public static ComboBox<String> getStandardComboBox(String string) {
		return getCombobox(string);
	}
	public static ComboBox<String> getStandardComboBox(String string,String width) {
		ComboBox<String> combobox = getCombobox(string);
		combobox.setWidth(width);
		return combobox;
	}

	/**
	 * 默认灰底黑字的中间提示3秒
	 */
	public static void notification(String text) {
		Notification.show(text, 3000, Position.MIDDLE);
	}
	/**
	 * 传入提示位置提示3秒
	 * @param text
	 * @param position
	 */
	public static void notification(String text,Position position) {
		Notification.show(text, 3000, position);
	}
	/**
	 * 成功是绿底色的中间提示3秒
	 */
	public static void notificationSuccess(String text) {
		Notification show = Notification.show(text, 3000, Position.MIDDLE);
		show.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}
	/**
	 * 失败是红色的中间提示，且需手动关闭
	 */
	public static void notificationError(String text) {
		Notification notification = new Notification();
		notification.add(text);
		notification.setPosition(Position.MIDDLE);
		notification.setDuration(5000);
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		notification.open();
	}

	/**
	 * 统一的异常提示：日志交给 log4j2，页面只显示一句人话。
	 * 用于替换散落在业务代码里的 e.printStackTrace()。
	 */
	public static void notificationError(String text, Throwable t) {
		org.slf4j.LoggerFactory.getLogger(ComFactory.class).error(text, t);
		notificationError(text + "：" + shortMessage(t));
	}

	private static String shortMessage(Throwable t) {
		if (null == t) {
			return "未知错误";
		}
		String msg = t.getMessage();
		if (null == msg || msg.isBlank()) {
			return t.getClass().getSimpleName();
		}
		return msg.length() > 120 ? msg.substring(0, 120) + "…" : msg;
	}
	/**
	 * primary蓝色提示3秒
	 * @param text
	 */
	public static void notificationPrimary(String text) {
		Notification show = Notification.show(text, 3000, Position.MIDDLE);
		show.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
	}
	/**
	 * 自定义提示信息、提示时间、提示类型
	 * @param text
	 * @param duration
	 * @param variant
	 */
	public static void notificationWithVariant(String text,int duration,NotificationVariant variant) {
		Notification show = Notification.show(text, duration, Position.MIDDLE);
		show.addThemeVariants(variant);
	}
}

	