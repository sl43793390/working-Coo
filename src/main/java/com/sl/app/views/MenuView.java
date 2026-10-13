package com.sl.app.views;

import jakarta.annotation.security.PermitAll;

import com.sl.app.component.editor.QuillEditor;
import com.sl.app.component.template.*;
import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.TabHeader;
import com.sl.app.component.util.TabsUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

//@Route(value = "dashboard", layout = MainLayout.class) 
@Route(value = "menu") 
//@PageTitle("gridView")
//@CssImport("./styles/shared-styles.css")
@PreserveOnRefresh
@PermitAll
public class MenuView extends VerticalLayout {
	private static final long serialVersionUID = 157718207780576753L;
	private TabSheet tabs;
	private static Integer count = 1;

	public MenuView() { 
//        addClassName("dashboard-view");
		addClassName("list-view-horizontalLayout");
        setDefaultHorizontalComponentAlignment(Alignment.START); 
        setMargin(false);
        setPadding(false);
        addheader();
        addMenu();
        addTabSheet();
    }

	private void addheader() {
		HorizontalLayout hlayout = new HorizontalLayout();
//		hlayout.setMargin(false);
//		hlayout.setPadding(false);
		hlayout.setWidth("98%");
		hlayout.setHeight("40px");
		Image img = new Image("img/2-home-page.png","");
		img.setWidth("20px");
		img.setHeight("18px");
		hlayout.add(img);
		
		Button logout = new Button("退出");
		hlayout.add(logout);
		logout.addClickListener(e ->{
			getUI().ifPresent(ui -> ui.navigate(""));
			VaadinSession.getCurrent().getSession().invalidate();
//			UI.getCurrent().getPage().setLocation("/logout");
		});
		add(hlayout);
	}

	private void addMenu() {
		MenuBar bar = new MenuBar();
		bar.addClassName("list-view-horizontalLayout");
		MenuItem addItem = bar.addItem("报告");
		MenuItem addItem2 = bar.addItem("管理");
		MenuItem addItem4 = bar.addItem("test管理");
		MenuItem addItem5 = bar.addItem("testtesm");
		MenuItem addItem9 = bar.addItem("用户管理");
		MenuItem addItem21 = addItem.getSubMenu().addItem("报告1");
		MenuItem addItem2111 = addItem.getSubMenu().addItem("日报");
//		addItem21.addClickListener(e ->Notification.show("提示", 2000, Position.MIDDLE));
		MenuItem addItem211 = addItem21.getSubMenu().addItem("报告1-1");
		addItem211.addClickListener(e ->addComponentTab(addItem211.getText(), ComFactory.getLabel(addItem211.getText())));
		MenuItem addItem22 = addItem2.getSubMenu().addItem("报告2");
		addItem22.addClickListener(e ->addComponentTab(addItem22.getText(), ComFactory.getLabel(addItem22.getText())));
		
		MenuItem userm = addItem9.getSubMenu().addItem("用户管理");
		userm.addClickListener(e ->{
//			Tab t = new Tab("用户管理");
			TabHeader h = new TabHeader(tabs, "用户管理");
			Tab t = new Tab(h);
			tabs.add(t,new Button("999"));
			tabs.setSelectedTab(t);
			count++;
		});
		add(bar);
		
	}


	private void addTabSheet() {
		
		Button btn = ComFactory.getPrimaryBtn("添加新tab");
//		btn.addClickListener(e ->tabs.remove(tabs.getSelectedTab()));
		btn.addClickListener(e ->addTab("tabDemo"+count ));
//		Tab payment = new Tab("Payment");
//		payment.setId("Payment");
//		Tab shipping = new Tab("Shipping");
//		shipping.setId("Shipping");
//		GridComponent g = new GridComponent();
//		details.add(g);
		tabs = new TabSheet();
		tabs.setWidthFull();
		GridComponent g = new GridComponent();
		addComponentTab("示例1", g,false);
		addComponentTab("添加tab", btn);
		TemplateComponent tm = new TemplateComponent();
		addComponentTab("模板", tm);
		TreeGridTemplateComponent tgt = new TreeGridTemplateComponent();
		addComponentTab("树结构", tgt);
		GridProDemo gridProDemo = new GridProDemo();
		addComponentTab("gridPro", gridProDemo);
		BoardDemo boardDemo = new BoardDemo();
		addComponentTab("Board响应式布局", boardDemo);
		ChartDemo chartDemo = new ChartDemo();
		addComponentTab("图表示例", chartDemo);
		RichTtextEditorDemo richText = new RichTtextEditorDemo();
		addComponentTab("富文本", richText);
		DialogDemo dialogDemo = new DialogDemo();
		addComponentTab("对话框", dialogDemo);
		CrudDemo crudDemo = new CrudDemo();
		addComponentTab("CRUD", crudDemo);
		UploadDemo uploadDemo = new UploadDemo();
		addComponentTab("文件上传/下载", uploadDemo);
		SignStatusDemo sign = new SignStatusDemo();
		addComponentTab("状态标记", sign);
		QuillEditor quillDemo = new QuillEditor().withToolbar("full").withHeight("300px");//Trix 编辑器已移除，换通用 Quill
		addComponentTab("富文本2(Quill)", quillDemo);
		ScrollDemo scrollDemo = new ScrollDemo();
		TabsUtil.addComponentTab(tabs, "滚动条", scrollDemo);
//		addComponentTab("滚动条", scrollDemo);
		TreeDemo treed = new TreeDemo();
		addComponentTab("树结构", treed);

		XtermDemo xterm = new XtermDemo();
		addComponentTab("Xterm终端模拟器",xterm);
		
		add(tabs);
//		 tabs.addSelectedChangeListener(
//	                event -> setContent(event.getSelectedTab()));
	}
	
	private void addTab(String text){
		Tab t = new Tab(new TabHeader(tabs, text));
		t.setId(text);
		tabs.add(t,new Span("无内容"));
		tabs.setSelectedTab(t);
		count++;
	}
	private void addComponentTab(String text,Component com){
		Tab t = new Tab(new TabHeader(tabs, text));
		t.setId(text);
		tabs.add(t,com);
		tabs.setSelectedTab(t);
		count++;
	}
	private void addComponentTab(String text,Component com,boolean closeable){
		Tab t = new Tab(new TabHeader(tabs, text,closeable));
		t.setId(text);
		tabs.add(t,com);
		tabs.setSelectedTab(t);
		count++;
	}
	
//	private void setContent(Tab tab) {
//        content.removeAll();
//        if (null == tab) {
//			return;
//		}
//        boolean present = tab.getId().isPresent();
//        if (!present) {
//        	return;
//		}
//        String text = tab.getId().get();
//        if (text.equals("Details")) {
//    		GridComponent g = new GridComponent();
//            content.add(g);
//        } else if (text.equals("Payment")) {
//            content.add(new Paragraph("This is the Payment tab"));
//        } else {
//            content.add(new Paragraph("This is the Shipping tab"));
//        }
//    }
}