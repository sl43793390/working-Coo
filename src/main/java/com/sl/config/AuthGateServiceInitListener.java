package com.sl.config;

import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sl.app.views.LoginView;
import com.sl.app.views.LoginViewV1;
import com.sl.app.views.LoginViewV3;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;

/**
 * 全局认证门卫。
 *
 * 背景：项目登录由 LoginView（@Route("")）内调用 Shiro Subject.login() 完成，
 * 登录状态存于 Shiro 会话 + VaadinSession（"user" 属性）。除登录页外没有任何
 * 视图做认证检查，因此应用重启后（Shiro 会话与 VaadinSession 全部丢失），浏览器
 * 刷新仍会直接渲染目标页面，不会跳回登录页。
 *
 * 做法：在每个 UI 上挂 BeforeEnterListener，导航目标不是公开视图时，
 * 检查 VaadinSession 的 "user" 属性或 Shiro Subject 是否已认证，
 * 都没有则 rerouteTo("") 回到登录页。
 */
@Component
public class AuthGateServiceInitListener implements VaadinServiceInitListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthGateServiceInitListener.class);

    /** 无需登录即可访问的视图（登录页本身及其历史版本） */
    private static final Set<Class<?>> PUBLIC_VIEWS =
            Set.of(LoginView.class, LoginViewV1.class, LoginViewV3.class);

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent ->
                uiEvent.getUI().addBeforeEnterListener(this::checkAuthentication));
    }

    private void checkAuthentication(BeforeEnterEvent event) {
        if (PUBLIC_VIEWS.contains(event.getNavigationTarget())) {
            return;
        }
        if (isLoggedIn()) {
            return;
        }
        logger.debug("未认证访问 {}，重定向回登录页", event.getLocation().getPath());
        event.rerouteTo("");
    }

    private boolean isLoggedIn() {
        // 1) VaadinSession 属性（LoginView 登录成功后写入）
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null && session.getAttribute("user") != null) {
            return true;
        }
        // 2) Shiro 认证状态（请求线程上由 Shiro 过滤器绑定 ThreadContext）
        try {
            Subject subject = SecurityUtils.getSubject();
            return subject != null && subject.isAuthenticated();
        } catch (Exception e) {
            // 线程上下文未绑定等极端情况，按未认证处理
            return false;
        }
    }
}
