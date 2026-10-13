package com.sl.app.appLayout;

import com.sl.app.component.util.ComFactory;
import com.sl.util.MenuContants;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
/**
 * 左侧是accordion可展开的类似手风琴样式，可隐藏。右侧是tabsheet;
 * @author Administrator
 *
 */
@Route("Accordion")
public class AppLayoutAccordion extends AppLayout {

	private static final long serialVersionUID = 5988417052518848565L;
	private TabSheet tabs;

	public AppLayoutAccordion() {
		H1 appTitle = new H1("MyApp");
		Icon icon = new Icon("../w52.png");
		appTitle.getStyle().set("font-size", "var(--lumo-font-size-l)").set("line-height", "var(--lumo-size-l)")
				.set("margin", "0 var(--lumo-space-m)");

		Accordion views = getPrimaryNavigation();//生成左侧栏

		DrawerToggle toggle = new DrawerToggle();//切换按钮

		TabSheet subViews = getSecondaryNavigation();

		HorizontalLayout wrapper = new HorizontalLayout(toggle);
//		wrapper.setAlignItems(FlexComponent.Alignment.START);
		wrapper.getStyle().set("height", "40px");
		wrapper.setSpacing(false);

		VerticalLayout viewHeader = new VerticalLayout(wrapper, subViews);
		viewHeader.setPadding(false);
		viewHeader.setSpacing(false);

		addToDrawer(icon, views);// 添加左侧抽屉栏
		addToNavbar(viewHeader);// 添加右侧主体区内容

		setPrimarySection(Section.DRAWER);
	}

	private Accordion getPrimaryNavigation() {
		Accordion accordion = new Accordion();
		accordion.addClassName("nav-accordion");//手风琴整体样式
		accordion.add("项目管理", createContent(
				createStyledAnchor("我的项目"), 
				createStyledAnchor("新增项目"),
				createStyledAnchor("项目统计分析")));

		accordion.add("任务管理", createContent(
				createStyledAnchor("我的任务"), 
				createStyledAnchor("新增任务"),
				createStyledAnchor("任务统计分析")));

		AccordionPanel financesPanel = new AccordionPanel();
		financesPanel.setSummaryText("用户管理");
		financesPanel.add(createContent(
				createStyledAnchor("用户列表"),
				createStyledAnchor("角色管理")));
		AccordionPanel backEndControl = new AccordionPanel();
		backEndControl.setSummaryText("后台管理");
		backEndControl.add(createContent(
				createStyledAnchor("系统设置"),
				createStyledAnchor("其它")));
		accordion.add(backEndControl);
		return accordion;
	}
	/**
	 * 添加每个菜单按钮的功能
	 * @param text
	 * @return
	 */
	private Button createStyledAnchor(String text) {
		Button anchor = ComFactory.getTertriayBtn(text);
		anchor.addClassName("nav-menu-btn");//全宽左对齐菜单项样式
		anchor.addClickListener(e ->{
			switch (text) {
			case MenuContants.MY_PROJECT:
				System.out.println("===");
				break;
			case MenuContants.PROJECT_ADD:
			case MenuContants.PROJECT_STATISTICS:
			case MenuContants.MY_TASK:
			case MenuContants.TASK_ADD:
			case MenuContants.TASK_STATISTICS:
			case MenuContants.USER_LIST:
			case MenuContants.ROLE_MANAGE:
			case MenuContants.SYSTEM_SETTING:
			case MenuContants.OTHER_SETTING:

			default:
				break;
			}
		});
			
//		RquestComponent.addTab(tabs, text));
		return anchor;
	}

	private TabSheet getSecondaryNavigation() {
		tabs = ComFactory.getTabSheet();
		tabs.addClassName("main-tabsheet");//tabs现代化样式
		return tabs;
	}

	private VerticalLayout createContent(Button... anchors) {
		VerticalLayout content = new VerticalLayout();
		content.setPadding(false);
		content.setSpacing(false);
		content.setAlignItems(Alignment.STRETCH);
		content.add(anchors);
		return content;
	}

}
