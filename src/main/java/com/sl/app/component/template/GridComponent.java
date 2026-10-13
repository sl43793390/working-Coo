package com.sl.app.component.template;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.sl.entity.User;
import com.sl.util.ImageUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;

import de.f0rce.signaturepad.SignaturePad;

@Service
@Scope("prototype")
public class GridComponent extends VerticalLayout {

	private static final long serialVersionUID = 3484771169201665428L;
	public GridComponent() { 
//        addClassName("dashboard-view");
        addClassName("list-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); 
        setMargin(false);
        addGrid();
//        addFormLayout();
        addGridLayout();
    }

	private void addGrid() {
        Span stats = new Span("contacts"); 
        stats.addClassNames("text-xl", "mt-m");
        add(stats);
        
        Grid<User> grid = new Grid<User>();
        grid.addColumn(User::getUserId).setHeader("用户ID");
        grid.addColumn(User::getUserName).setHeader("用户名");
        grid.addColumn(User::getCdPhone).setHeader("电话");
        grid.addColumn(User::getEmail).setHeader("邮箱");
        grid.addColumn(User::getOrganization).setHeader("机构");
        User u = new User();
        u.setUserId("qweqwe");
        u.setUserName("真名");
        u.setCdPhone("123123123123");
        u.setEmail("qweqwe@234.com");
        u.setOrganization("公司1232111");
        grid.setItems(u);
        add(grid);
    }


	 private void addGridLayout() {
	    	HorizontalLayout layout = new HorizontalLayout();
	    	layout.addClassName("list-view-horizontalLayout");
	    	add(layout);
	    	Button bu = new Button("确定");
	    	bu.setWidth("120px");
	    	bu.setIcon(new Icon(VaadinIcon.CONNECT));
	    	layout.add(bu);
	    	bu.addClickListener(e -> {
	    		boolean permitted = SecurityUtils.getSubject().isPermitted("abc");
	    		if (permitted) {
	    			Notification.show("welcome", 5000, Notification.Position.MIDDLE);
				}else {
					Notification.show("无权限", 5000, Notification.Position.MIDDLE);
				}
	    		
	    	});
	    	Button bu2 = new Button("确定2");
	    	bu2.setWidth("120px");
	    	bu2.setIcon(new Icon(VaadinIcon.CONNECT));
	    	layout.add(bu2);
	    	Button bu3 = new Button("确定3");
	    	bu3.setWidth("120px");
	    	bu3.setIcon(new Icon(VaadinIcon.CONNECT));
	    	layout.add(bu3);
	    	
	    	
	    	TextField field = new TextField("测试标签");
	    	layout.add(field);
	    	
	    	MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
	    	listBox.setItems("qwe", "345e", "sdfsss");
	    	listBox.select("qwe");
	    	layout.add(listBox);
	    	
	    	RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
	    	radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
	    	radioGroup.setLabel("Travel class");
	    	radioGroup.setItems("Economy", "Business", "First Class");
	    	layout.add(radioGroup);
	    	
	    	ComboBox<String> com = new ComboBox<String>("com");
	    	com.setItems("com1","com2","com3");
	    	layout.add(com);
	    	//手写签名
	    	
	    	SignaturePad signature = new SignaturePad();
	    	Button rewrite = new Button("重写");
	    	rewrite.setWidth("120px");
	    	rewrite.setIcon(new Icon(VaadinIcon.PENCIL));
	    	layout.add(rewrite);
	    	rewrite.addClickListener(e -> signature.clear());
	    	Button save = new Button("保存");
	    	save.setWidth("120px");
	    	save.setIcon(new Icon(VaadinIcon.SAFE));
	    	layout.add(save);
	    	save.addClickListener(e -> {
	    		byte[] imageBase64 = signature.getImageBase64();
	    		ImageUtil.generateImage(imageBase64, "123.png", "D://");
	    	});
	    	
	    	signature.setHeight("350px");
	    	signature.setWidth("300px");
	    	signature.setBackgroundColor(0, 0, 0, 0);
	    	signature.setPenColor("#000000");
	    	layout.add(signature);
	    	
		}
    private void addFormLayout() {
    	FormLayout layout = new FormLayout();
    	add(layout);
    	Button bu = new Button("确定");
    	bu.setWidth("120px");
    	bu.setIcon(new Icon(VaadinIcon.CONNECT));
    	layout.add(bu);
    	bu.addClickListener(e -> Notification.show("welcome", 5000, Notification.Position.MIDDLE));
    	Button bu2 = new Button("确定2");
    	bu2.setWidth("120px");
    	bu2.setIcon(new Icon(VaadinIcon.CONNECT));
    	layout.add(bu2);
    	Button bu3 = new Button("确定3");
    	bu3.setWidth("120px");
    	bu3.setIcon(new Icon(VaadinIcon.CONNECT));
    	layout.add(bu3);
    	
    	
    	TextField field = new TextField("测试标签");
    	layout.add(field);
    	
    	MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
    	listBox.setItems("qwe", "345e", "sdfsss");
    	listBox.select("qwe");
    	layout.add(listBox);
    	
    	RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
    	radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
    	radioGroup.setLabel("Travel class");
    	radioGroup.setItems("Economy", "Business", "First Class");
    	layout.add(radioGroup);
	}
}