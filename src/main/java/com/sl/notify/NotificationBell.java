package com.sl.notify;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Notification;
import com.sl.entity.Task;
import com.sl.service.NotificationService;
import com.sl.service.TaskProcess;
import com.sl.task.TaskDetailComponent;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 顶栏的通知铃铛（T2-4）。
 * <p>
 * 未读数挂在按钮文本上（红点，样式见 styles.css 的 .notification-bell-unread）。
 * <p>
 * 刷新策略（S3 升级）：服务端写入新通知时经 {@link NotificationBroadcaster} 经
 * Push（WebSocket）实时推送刷新；推送不可达时降级为低频轮询（60 秒）兜底。
 * bell 是 prototype bean，每次导航都会新建，所以订阅/轮询的注册/摘除必须放在
 * onAttach/onDetach：导航走后不摘 listener，会留着无效的订阅与空查询。
 */
@SpringComponent
@Scope("prototype")
public class NotificationBell extends Button implements NotificationBroadcaster.Listener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(NotificationBell.class);

	private static final int PAGE_SIZE = 20;

	/** 兜底轮询间隔（毫秒）。Push 正常时用不上，只在 WebSocket 不可达时生效 */
	private static final int POLL_INTERVAL_MS = 60_000;

	private final NotificationService notificationService;
	private final TaskProcess taskProcess;
	private final NotificationBroadcaster broadcaster;

	private Dialog dialog;
	private Grid<Notification> grid;
	private Span emptyHint;
	/** 兜底轮询监听句柄，detach 时摘掉 */
	private Registration pollRegistration;
	/** Push 广播订阅句柄，detach 时摘掉 */
	private Registration broadcastRegistration;

	public NotificationBell(@Autowired NotificationService notificationService,
			@Autowired TaskProcess taskProcess,
			@Autowired NotificationBroadcaster broadcaster) {
		this.notificationService = notificationService;
		this.taskProcess = taskProcess;
		this.broadcaster = broadcaster;
		setIcon(new Icon(VaadinIcon.BELL));
		addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
		addClassName("notification-bell");
		setTooltipText("通知");
		getElement().setAttribute("aria-label", "通知");
		addClickListener(e -> openDialog());
		refreshBadge();
	}

	/**
	 * 挂到 UI 后：订阅实时广播 + 保留低频轮询兜底。
	 * poll 回调在 VaadinSession 锁内执行，直接 setText 改按钮状态是安全的。
	 */
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		UI ui = attachEvent.getUI();
		ui.setPollInterval(POLL_INTERVAL_MS);
		pollRegistration = ui.addPollListener(e -> refreshBadge());
		broadcastRegistration = broadcaster.addListener(this);
	}

	/** 视图销毁（导航离开/退出登录）时摘掉订阅与轮询，别留后台空查询 */
	@Override
	protected void onDetach(DetachEvent detachEvent) {
		super.onDetach(detachEvent);
		if (null != pollRegistration) {
			pollRegistration.remove();
			pollRegistration = null;
		}
		if (null != broadcastRegistration) {
			broadcastRegistration.remove();
			broadcastRegistration = null;
		}
		detachEvent.getUI().setPollInterval(-1);
	}

	@Override
	public String getUserId() {
		return currentUserId();
	}

	/** 广播回调：已在 UI.access 锁内，直接刷新红点 */
	@Override
	public void onNotification() {
		refreshBadge();
	}

	/** 外部（例如任务保存后）可以调它刷新红点 */
	public void refreshBadge() {
		String userId = currentUserId();
		if (null == userId) {
			setText("");
			removeClassName("notification-bell-unread");
			return;
		}
		long unread = notificationService.countUnread(userId);
		if (unread > 0) {
			setText(unread > 99 ? "99+" : String.valueOf(unread));
			addClassName("notification-bell-unread");
		} else {
			setText("");
			removeClassName("notification-bell-unread");
		}
	}

	private void openDialog() {
		if (null == dialog) {
			buildDialog();
		}
		reload();
		dialog.open();
	}

	private void buildDialog() {
		dialog = new Dialog();
		dialog.setHeaderTitle("通知");
		dialog.setWidth("760px");
		dialog.setHeight("520px");
		dialog.setCloseOnOutsideClick(true);

		VerticalLayout content = new VerticalLayout();
		content.setPadding(false);
		content.setSizeFull();

		grid = ComFactory.getGrid(Notification.class);
		grid.setHeight("380px");
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addColumn(Notification::getTypeName).setHeader("类型").setWidth("100px");
		grid.addColumn(n -> null == n.getCdTitle() ? "" : n.getCdTitle()).setHeader("标题").setResizable(true);
		grid.addColumn(n -> null == n.getCdContent() ? "" : n.getCdContent()).setHeader("内容").setResizable(true);
		grid.addColumn(n -> "N".equals(n.getFlagRead()) ? "未读" : "已读").setHeader("状态").setWidth("80px");
		grid.addComponentColumn(n -> {
			Button b = ComFactory.getTertriayBtn("查看", VaadinIcon.LIST);
			b.setVisible(Notification.TYPE_ASSIGN.equals(n.getCdType())
					|| Notification.TYPE_DUE.equals(n.getCdType())
					|| Notification.TYPE_COMMENT.equals(n.getCdType()));
			b.addClickListener(e -> openBiz(n));
			return b;
		}).setHeader("跳转").setWidth("90px");
		grid.addComponentColumn(n -> {
			Button b = ComFactory.getTertriayBtn("已读", VaadinIcon.CHECK);
			b.setVisible(!"Y".equals(n.getFlagRead()));
			b.addClickListener(e -> {
				// 必须带当前用户：markRead 的 SQL 有 ID_USER 条件，防止越权标记别人的通知
				String userId = currentUserId();
				if (null != userId) {
					notificationService.markRead(n.getIdNotification(), userId);
				}
				reload();
			});
			return b;
		}).setHeader("标记").setWidth("90px");
		content.add(grid);

		emptyHint = ComFactory.getEmptyHint("暂无通知");
		content.add(emptyHint);

		Button markAll = ComFactory.getPrimaryBtn("全部标记已读");
		// .vaadin-button-back 写死 width:90px，「全部标记已读」6 个汉字放不下会被截断；
		// 内联样式优先级高于类样式，放开宽度并保留 90px 下限，和「关闭」按钮视觉一致
		markAll.getStyle().set("width", "auto").set("min-width", "90px");
		markAll.addClickListener(e -> {
			String userId = currentUserId();
			if (null == userId) {
				return;
			}
			notificationService.markAllRead(userId);
			reload();
		});
		Button close = ComFactory.getPrimaryBtn("关闭");
		close.addClickListener(e -> dialog.close());
		dialog.getFooter().add(markAll, close);
		dialog.add(content);
	}

	private void reload() {
		String userId = currentUserId();
		if (null == userId) {
			return;
		}
		List<Notification> list;
		try {
			list = notificationService.page(userId, null, 0, PAGE_SIZE);
		} catch (Exception e) {
			log.error("加载通知失败", e);
			ComFactory.notificationError("加载通知失败", e);
			return;
		}
		grid.setItems(list);
		ComFactory.toggleEmptyHint(emptyHint, list.size());
		refreshBadge();
	}

	private void openBiz(Notification n) {
		if (null == n.getIdBiz() || n.getIdBiz().isBlank()) {
			return;
		}
		Task t = taskProcess.getTaskById(n.getIdBiz());
		if (null == t) {
			ComFactory.notification("关联任务不存在或已被删除");
			return;
		}
		String userId = currentUserId();
		if (null != userId) {
			notificationService.markRead(n.getIdNotification(), userId);
		}
		dialog.close();
		TaskDetailComponent c = ComponentUtil.applicationContext.getBean(TaskDetailComponent.class);
		c.setTask(t);
		c.initContent();
		TabsUtil.addComponentTab("任务详情-" + t.getIdTask(), "任务详情", c);
		refreshBadge();
	}

	private String currentUserId() {
		return null == ComponentUtil.getCurrentUser() ? null : ComponentUtil.getCurrentUser().getUserId();
	}
}
