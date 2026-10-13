package com.sl.app.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.sl.entity.User;
import com.sl.mapper.UserMapper;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "loginv1")
//@AnonymousAllowed
public class LoginViewV1 extends VerticalLayout {

    private static final long serialVersionUID = 8728059005569047780L;
	private LoginForm form;
	
	@Autowired
	private UserMapper userMapper;
    public LoginViewV1() {
    	setJustifyContentMode(JustifyContentMode.CENTER);
      setAlignItems(Alignment.CENTER);
  	LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("my app");
		i18n.getHeader().setDescription("欢迎。。。");
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("登录");
		i18n.getForm().setTitle("xxx系统");
		i18n.getForm().setUsername("用户名：");
		i18n.getForm().setPassword("密码");
		form = new LoginForm(i18n);
		form.setAction("login");
		add(form);
//		form.setError(true);
		form.addLoginListener(new ComponentEventListener<AbstractLogin.LoginEvent>() {
			
			@Override
			public void onComponentEvent(AbstractLogin.LoginEvent event) {
				// TODO Auto-generated method stub
				User selectByPrimaryKey = userMapper.selectByPrimaryKey(event.getUsername());
				System.out.println(selectByPrimaryKey.toString());
				System.out.println(event.getUsername());
				System.out.println(event.getPassword());
				UI.getCurrent().getPage().setLocation("/menu");
			}
		});
    }


}
