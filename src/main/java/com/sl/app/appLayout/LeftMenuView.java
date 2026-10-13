package com.sl.app.appLayout;

import com.sl.app.component.util.ComFactory;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
/**
 * 自定义的一个视图，左侧是固定宽度的一个bar用来存放一些图标按钮，右侧展示内容
 * @author Administrator
 *
 */
@Route("leftview")
public class LeftMenuView extends HorizontalLayout{

	private static final long serialVersionUID = 5749128329402975256L;

	public LeftMenuView() {
		addLeftBar();
	}

	private void addLeftBar() {
		this.setHeight("100%");
		this.setJustifyContentMode(JustifyContentMode.START);
		VerticalLayout v = new VerticalLayout();
		v.setDefaultHorizontalComponentAlignment(Alignment.CENTER);//将vertical中的组件水平居中
		v.addClassName("dark-background");
		v.setSpacing(false);
		v.setJustifyContentMode(JustifyContentMode.START);//将vertical中的组件靠上放置
		v.setWidth("70px");
		
		H4 h4 = new H4("title");
		h4.getStyle().set("color", "white").set("height", "30px");
		v.add(h4);
		Icon icon1 = new Icon(VaadinIcon.CALENDAR);
		icon1.getStyle().set("height", "40px");
		v.add(icon1);
		icon1.addClickListener(e -> System.out.println("====="));
		Icon icon2 = new Icon(VaadinIcon.CALC_BOOK);
		icon2.getStyle().set("height", "40px");
		v.add(icon2);
		icon2.addClickListener(e -> System.out.println("====="));
		this.add(v);

//		添加右侧的组件
		VerticalLayout v2 = new VerticalLayout();
		v2.getStyle().set("width", "1000px");//必须设置宽度
		v2.setJustifyContentMode(JustifyContentMode.START);
		v2.add(ComFactory.getTertriayBtn("btn1"));
		v2.add(ComFactory.getTertriayBtn("btn2"));
		v2.add(ComFactory.getTertriayBtn("btn3"));
		Anchor an = new Anchor("/file/download?fileId=123321221", "下载", AnchorTarget.BLANK);
		v2.add(an);
		this.add(v2);
	}
	
}
