package com.sl.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sl.app.component.util.ComFactory;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

/**
 * 全局 UI 异常处理。
 *
 * 背景：业务代码里大量 try-catch 后只做 e.printStackTrace() 或干脆吞掉，
 * 页面上没有反馈、日志里也查不到堆栈。这里给每个 UI 挂一个兜底的 ErrorHandler：
 * 统一记日志 + 给用户一条明确的错误提示，避免出现「点了没反应」的空白失败。
 */
@Component
public class UiErrorHandler implements VaadinServiceInitListener {

	private static final Logger log = LoggerFactory.getLogger(UiErrorHandler.class);

	@Override
	public void serviceInit(ServiceInitEvent event) {
		// Vaadin 的错误处理器挂在 VaadinSession 上（UI 本身没有 setErrorHandler）
		event.getSource().addUIInitListener(uiEvent -> {
			VaadinSession session = uiEvent.getUI().getSession();
			if (null != session) {
				session.setErrorHandler(this::handle);
			}
		});
	}

	private void handle(ErrorEvent errorEvent) {
		Throwable t = errorEvent.getThrowable();
		if (null == t) {
			return;
		}
		log.error("UI 未捕获异常", t);
		ComFactory.notificationError("操作失败：" + rootMessage(t));
	}

	/**
	 * 取根因的简短描述，避免把一长串类名直接甩给用户。
	 */
	private static String rootMessage(Throwable t) {
		Throwable root = t;
		while (null != root.getCause() && root.getCause() != root) {
			root = root.getCause();
		}
		String msg = root.getMessage();
		if (null == msg || msg.isBlank()) {
			return root.getClass().getSimpleName();
		}
		return msg.length() > 120 ? msg.substring(0, 120) + "…" : msg;
	}
}
