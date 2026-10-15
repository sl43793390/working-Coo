package com.sl.notify;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.shared.Registration;

/**
 * 通知实时广播器（S3）。
 * <p>
 * 通知写入方（NotificationService）调 {@link #broadcast(String)}，
 * 该用户打开着的所有 UI（多标签页）立即收到回调刷新铃铛红点——不再等 15 秒轮询。
 * <p>
 * 回调必须经 {@code UI.access(...)} 切回 VaadinSession 锁内执行，
 * 这里统一处理，监听方只需提供刷新逻辑。
 * 推送失败（UI 已关闭等）按 Vaadin 约定静默处理，轮询兜底仍然有效。
 */
@Component
public class NotificationBroadcaster {

	private static final Logger log = LoggerFactory.getLogger(NotificationBroadcaster.class);

	/** 铃铛等订阅方实现的回调接口 */
	public interface Listener {
		String getUserId();

		/** 在 UI.access 锁内执行，直接改组件状态即可 */
		void onNotification();
	}

	private record Entry(UI ui, Listener listener) {
	}

	private final Map<String, List<Entry>> listenersByUser = new ConcurrentHashMap<>();

	public Registration addListener(Listener listener) {
		UI ui = UI.getCurrent();
		if (null == ui || null == listener.getUserId()) {
			return () -> {
			};
		}
		Entry entry = new Entry(ui, listener);
		listenersByUser.computeIfAbsent(listener.getUserId(), k -> new CopyOnWriteArrayList<>()).add(entry);
		log.debug("通知监听注册：user={}，当前 {} 个连接", listener.getUserId(),
				listenersByUser.get(listener.getUserId()).size());
		return () -> removeEntry(listener.getUserId(), entry);
	}

	private void removeEntry(String userId, Entry entry) {
		List<Entry> list = listenersByUser.get(userId);
		if (null != list) {
			list.remove(entry);
			if (list.isEmpty()) {
				listenersByUser.remove(userId, list);
			}
		}
	}

	/**
	 * 通知某用户：其所有打开的 UI 在各自的会话锁内收到 onNotification 回调。
	 * 推送是「尽力而为」：用户不在线就没人收到，靠轮询/下次打开页面兜底。
	 */
	public void broadcast(String userId) {
		if (null == userId || userId.isBlank()) {
			return;
		}
		List<Entry> list = listenersByUser.get(userId);
		if (null == list || list.isEmpty()) {
			return;
		}
		for (Entry entry : list) {
			UI ui = entry.ui();
			if (null == ui || ui.isClosing()) {
				removeEntry(userId, entry);
				continue;
			}
			try {
				ui.access(entry.listener()::onNotification);
			} catch (Exception e) {
				// UI 已失效（浏览器关了/会话过期）：推送失败不影响通知写入
				log.debug("推送通知到 UI 失败（可能已关闭），user={}", userId);
				removeEntry(userId, entry);
			}
		}
	}
}
