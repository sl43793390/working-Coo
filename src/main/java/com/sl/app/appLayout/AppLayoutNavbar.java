package com.sl.app.appLayout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
/**
 * 上面是一个居中的tab，下面用于展示内容，适用于页面较少功能较少的应用
 * @author Administrator
 *
 */
@Route("applayout1")
public class AppLayoutNavbar extends AppLayout {

	private static final long serialVersionUID = 6924011052891727145L;

	public AppLayoutNavbar() {
        H1 title = new H1("MyApp");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)").set("margin", "0")
                .set("position", "absolute");

        Tabs tabs = getTabs();
        
        addToNavbar(title, tabs);
    }
    // end::snippet[]

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(createTab("Dashboard"),
        		createTab("Orders"),
                createTab("Customers"),
                createTab("Products"));
        return tabs;
    }

    private Tab createTab(String viewName) {
        RouterLink link = new RouterLink();
        link.add(viewName);
        // Demo has no routes
        // link.setRoute(viewClass.java);
        link.setTabIndex(-1);

        Tab tab = new Tab(link);
        tab.add(new Span(viewName));
        return tab;
    }

}
