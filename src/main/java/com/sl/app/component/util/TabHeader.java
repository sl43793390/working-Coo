package com.sl.app.component.util;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.TabSheet;

public class TabHeader extends HorizontalLayout{

	private Button closeButton;

	/**
	 * Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
		plusButton.addThemeVariants(ButtonVariant.LUMO_ICON);
		plusButton.getElement().setAttribute("aria-label", "Add item");
		
		Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
		closeButton.addThemeVariants(ButtonVariant.LUMO_ICON);
		closeButton.getElement().setAttribute("aria-label", "Close");
		
		Button arrowLeftButton = new Button("Left",
		        new Icon(VaadinIcon.ARROW_LEFT));
		
		Button arrowRightButton = new Button("Right",
		        new Icon(VaadinIcon.ARROW_RIGHT));
		arrowRightButton.setIconAfterText(true);
	 * @param title
	 */
	public TabHeader(TabSheet tab,String title) {
		Span lb = new Span(title);
		lb.addClassName("tab-title-label");
		setSpacing(false);
		closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
		closeButton.addClassName("tab-header-close-btn");
		closeButton.addThemeVariants(ButtonVariant.LUMO_ICON);
		closeButton.getElement().setAttribute("aria-label", "Close");
		setAlignItems(Alignment.CENTER);
		add(lb,closeButton);
		closeButton.addClickListener(e -> tab.remove(tab.getSelectedTab()));
	}
	/**
	 * closeable 默认true ,传入FALSE则代表tab不能关闭
	 * @param tab
	 * @param title
	 * @param closeable
	 */
	public TabHeader(TabSheet tab,String title,boolean closeable) {
		Span lb = new Span(title);
		lb.addClassName("tab-title-label");
		setAlignItems(Alignment.CENTER);
		setSpacing(false);
		if (closeable) {
			closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
			closeButton.addClassName("tab-header-close-btn");
			closeButton.addThemeVariants(ButtonVariant.LUMO_ICON);
			closeButton.getElement().setAttribute("aria-label", "Close");
			closeButton.addClickListener(e -> tab.remove(tab.getSelectedTab()));
			add(lb,closeButton);
		}else {
			add(lb);
		}
	}
	
	public void setCloseable(boolean flag) {
		if (flag) {
			remove(closeButton);
		}
	}
}
