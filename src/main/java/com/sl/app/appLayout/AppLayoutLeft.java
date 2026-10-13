package com.sl.app.appLayout;

import com.sl.app.component.util.ComFactory;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
/**
 * 左侧是tab加了个图标和文字，可隐藏。右侧是tabsheet;
 * @author Administrator
 *
 */
@Route("left")
public class AppLayoutLeft extends AppLayout {

	private static final long serialVersionUID = 5988417052518848565L;
	private TabSheet tabsheet;

	public AppLayoutLeft() {
		H1 appTitle = new H1("MyApp");
		appTitle.getStyle().set("font-size", "var(--lumo-font-size-l)").set("line-height", "var(--lumo-size-l)")
				.set("margin", "0 var(--lumo-space-m)");

		Tabs views = getPrimaryNavigation();//生成左侧栏

		DrawerToggle toggle = new DrawerToggle();//切换按钮
		TabSheet subViews = getSecondaryNavigation();

		HorizontalLayout wrapper = new HorizontalLayout(toggle);
//		wrapper.setAlignItems(FlexComponent.Alignment.START);
		wrapper.getStyle().set("height", "40px");
		wrapper.setSpacing(false);

		VerticalLayout viewHeader = new VerticalLayout(wrapper, subViews);
		viewHeader.setPadding(false);
		viewHeader.setSpacing(false);

		addToDrawer(appTitle, views);// 添加左侧抽屉栏
		addToNavbar(viewHeader);// 添加右侧主体区内容

		setPrimarySection(Section.DRAWER);
	}

	private Tabs getPrimaryNavigation() {
		 Tabs tabs = new Tabs();
	        tabs.add(createTab(VaadinIcon.DASHBOARD, "控制台"),
	                createTab(VaadinIcon.CART, "成员管理"),
	                createTab(VaadinIcon.USER_HEART, "角色管理"),
	                createTab(VaadinIcon.PACKAGE, "应用管理"),
	                createTab(VaadinIcon.RECORDS, "服务管理"),
	                createTab(VaadinIcon.LIST, "企业设置"),
	                createTab(VaadinIcon.CHART, "数据管理"));
	        tabs.setOrientation(Tabs.Orientation.VERTICAL);
	        tabs.setSelectedIndex(1);
	        tabs.addSelectedChangeListener(new ComponentEventListener<Tabs.SelectedChangeEvent>() {
				
				@Override
				public void onComponentEvent(SelectedChangeEvent event) {
					String text = event.getSelectedTab().getElement().getTextRecursively();
					 ComFactory.addTab(tabsheet, text);
				}
			});
	        return tabs;
	}
	 private Tab createTab(VaadinIcon viewIcon, String viewName) {
	        Icon icon = viewIcon.create();
	        icon.getStyle().set("box-sizing", "border-box")
	                .set("margin-inline-end", "var(--lumo-space-m)")
	                .set("padding", "var(--lumo-space-xs)");

	        RouterLink link = new RouterLink();
	        link.add(icon, new Span(viewName));
	        // Demo has no routes
	        // link.setRoute(viewClass.java);
	        link.setTabIndex(-1);
	        Tab tab = new Tab(link);
	        return tab;
	    }
	private TabSheet getSecondaryNavigation() {
		tabsheet = ComFactory.getTabSheet();
		return tabsheet;
	}

	private VerticalLayout createContent(Button... anchors) {
		VerticalLayout content = new VerticalLayout();
		content.setPadding(true);
		content.setSpacing(true);
		content.add(anchors);
		return content;
	}

	private Button createStyledAnchor(String text) {
		Button anchor = ComFactory.getTertriayBtn(text);
		anchor.setWidth("50%");
		anchor.getStyle().set("color", "var(--lumo-primary-text-color)");
		anchor.getStyle().set("font-size", "16px");
		anchor.addClickListener(e -> ComFactory.addTab(tabsheet, text));
		return anchor;
	}

}
