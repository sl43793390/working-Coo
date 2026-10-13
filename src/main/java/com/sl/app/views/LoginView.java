package com.sl.app.views;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sl.entity.User;
import com.sl.security.LoginAttemptService;
import com.sl.service.UserService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "")
public class LoginView extends LoginOverlay {
//	@AnonymousAllowed, @PermitAll, @RolesAllowed.
	
	private static final Logger logger = LoggerFactory.getLogger(LoginView.class);
	
	@Autowired
	private LoginAttemptService loginAttemptService;

    public LoginView(@Autowired UserService userService) {

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("系统登录");
        i18n.getHeader().setDescription("提示：");
        i18n.getForm().setTitle("WorkingCoo");
        i18n.getForm().setSubmit("登录");
        i18n.setAdditionalInformation(null);
        // 登录失败时表单内联展示的错误文案。createDefault() 自带英文默认值，这里补成中文；
        // 具体原因（用户名不存在 / 剩余尝试次数 / 账号已锁定）仍由下面的 Notification 提示。
        if (null == i18n.getErrorMessage()) {
            i18n.setErrorMessage(new LoginI18n.ErrorMessage());
        }
        i18n.getErrorMessage().setTitle("用户名或密码不正确");
        i18n.getErrorMessage().setMessage("请检查后重试");
        setI18n(i18n);
        
        setForgotPasswordButtonVisible(false);
        setOpened(true);
        addLoginListener(new ComponentEventListener<AbstractLogin.LoginEvent>() {
			
			private static final long serialVersionUID = 3541080283633713772L;

			@Override
			public void onComponentEvent(LoginEvent event) {
				Notification notification = new Notification();
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
//				进行认证操作
				 // T3-2：这里传明文，哈希交给 WorkingCooCredentialsMatcher 做。
				 // 旧写法是在前端先 SmUtil.sm3 再丢给 Shiro，等于把"用哪种算法"硬编码在登录页，
				 // 想换算法就必须改登录页，而且 SM3 没有 salt —— 所以改成后端统一 Hasher 处理。
			        UsernamePasswordToken token = new UsernamePasswordToken(event.getUsername(), event.getPassword());
			        //获取当前的Subject  
			        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();  
			        try {  
			            logger.debug("对用户[" + event.getUsername() + "]进行登录验证..验证开始");  
			            currentUser.login(token);  
			            logger.debug("对用户[" + event.getUsername() + "]进行登录验证..验证通过");  
			        }catch(UnknownAccountException uae){  
			            // 失败必须 setError(true)：vaadin-login-form 的 submit() 发出 login 事件时会先把
			            // 登录按钮 disabled=true 防重复提交，解除禁用只有两条路——error 属性变 true
			            // （客户端 _errorChanged；服务端 setError 内部还会顺带 setEnabled(true)），
			            // 或遮罩关闭重开。只弹 Notification 不 setError，输错一次密码按钮就永久置灰。
			            LoginView.this.setError(true);
			        	notification.show("用户名不存在",3000,Position.MIDDLE);
			            return;
			        }catch(IncorrectCredentialsException ice){
			        	LoginView.this.setError(true);
			        	// T3-3：失败已被限流服务计数，这里把剩余次数提示出来，别让用户干猜
			        	int left = loginAttemptService.remainAttempts(event.getUsername());
			        	notification.show(left > 0
			        			? "用户名或密码不正确，还可尝试 " + left + " 次"
			        			: "用户名或密码不正确", 3000, Position.MIDDLE);
			        	  return;
			        }catch(LockedAccountException lae){
			        	LoginView.this.setError(true);
			        	long sec = loginAttemptService.remainSeconds(event.getUsername());
			        	notification.show(sec > 0
			        			? "失败次数过多，账号已锁定，请 " + (sec / 60 + 1) + " 分钟后再试"
			        			: "账号已锁定，请稍后再试", 4000, Position.MIDDLE);
			        	  return;
			        }catch(ExcessiveAttemptsException eae){
			        	LoginView.this.setError(true);
			        	notification.show("密码输入次数过多",3000,Position.MIDDLE);
			        	  return;
			        }catch(AuthenticationException ae){  
			            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景  
			            LoginView.this.setError(true);
			            ae.printStackTrace();  
			            notification.show("用户名或密码不正确",3000,Position.MIDDLE);
			            return;
			        }  
			        User user = userService.getUserById(event.getUsername());
			        VaadinSession.getCurrent().setAttribute("user",user) ;
					VaadinSession.getCurrent().setAttribute("userName", event.getUsername());
						logger.info("用户{}登录成功", event.getUsername());
//						UI.getCurrent().getPage().setLocation("/menu");
//						event.getSource().getUI().ifPresent(ui -> ui.navigate("/menu"));
						getUI().ifPresent(ui -> ui.navigate("/projectView"));
//						getUI().ifPresent(ui -> ui.navigate("/leftview"));
			}
		});
    }

}
