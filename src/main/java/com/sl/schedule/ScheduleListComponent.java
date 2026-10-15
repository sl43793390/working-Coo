package com.sl.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.editor.QuillEditor;
import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.Schedule;
import com.sl.service.ScheduleProcess;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;

@SpringComponent
@Scope("prototype")
public class ScheduleListComponent extends VerticalLayout {

	private static final long serialVersionUID = 9071323139692234287L;

	/** 卡片标题最多显示的字数，超出部分截断加省略号 */
	private static final int TITLE_MAX_LEN = 15;
	
	private ScheduleProcess scheduleProcess;
	private List<Schedule> scheduleByUserId;
	private VerticalLayout mainLayout;

	private ConfirmDialog dialog;

	public ScheduleListComponent(@Autowired ScheduleProcess scheduleProcess) {
		this.scheduleProcess = scheduleProcess;
		setDefaultHorizontalComponentAlignment(Alignment.START); 
		setWidth("100%");
		setHeight("770px");
        setMargin(false);
        mainLayout = ComFactory.getVerticalLayout();
		mainLayout.setWidth("1600px");
//		创建滚动窗口
		Scroller sc = new Scroller(mainLayout);
		sc.setScrollDirection(ScrollDirection.VERTICAL);
		add(sc);//放入大layout中
		
        initContent();
	}

	public void initContent() {
		mainLayout.removeAll();
		mainLayout.add(ComFactory.getTitle("我的代办清单"));
		mainLayout.add(ComFactory.getHorizontalLine());
		scheduleByUserId = scheduleProcess.getScheduleByUserId(ComponentUtil.getCurrentUser().getUserId());
		int count = null == scheduleByUserId ? 0 : scheduleByUserId.size();
		Span emptyHint = ComFactory.getEmptyHint("暂无待办日程");
		mainLayout.add(emptyHint);
		ComFactory.toggleEmptyHint(emptyHint, count);
		for (int i = 0; i < count; i++) {
			mainLayout.add(generateCard(scheduleByUserId.get(i)));
		}
	}

	/**
	 * 日程卡片：普通 Div 头部一行「头像 + 标题(15字截断) + 时间 pill + 右侧竖排(过期徽标/删除)」，
	 * 备注有内容时渲染成引用块。样式见 styles.css 的 .schedule-card 系列。
	 */
	private VerticalLayout generateCard(Schedule schedule) {
		VerticalLayout card = new VerticalLayout();
		card.addClassName("schedule-card");
		card.setPadding(false);
		card.setSpacing(false);
		card.setMargin(false);

		// 头部改用普通 Div 手写 flex：主题的 components/vaadin-*-layout.css 给所有
		// Horizontal/VerticalLayout 强设 width:100% + space-between + wrap，连日三次
		// 把头部搞乱（标题挤成竖排/挤没、右侧组顶不到边）。普通 Div 不在主题的
		// 组件样式打击范围内，布局行为完全由 .schedule-card-header 自己说了算。
		Div header = new Div();
		header.addClassName("schedule-card-header");
		header.add(ComFactory.getNamedAvatar(schedule.getNameSchedule()));

		// 标题在 Java 里截断：最多 15 字，超出加省略号，悬停看全名。
		// 靠 CSS 收缩长文不可靠——CJK 文本在 flex 里被压缩会变一字一列甚至零宽。
		String name = schedule.getNameSchedule();
		String displayName = name;
		if (null != name && name.length() > TITLE_MAX_LEN) {
			displayName = name.substring(0, TITLE_MAX_LEN) + "…";
		}
		Span title = new Span(displayName);
		title.addClassName("schedule-card-title");
		if (null != name && !name.equals(displayName)) {
			title.getElement().setAttribute("title", name);
		}
		header.add(title);

		boolean overdue = null != schedule.getDtSchedule() && schedule.getDtSchedule().before(new Date());

		// 时间 pill：浅绿底圆角，紧跟标题；已过期的整枚变红底红字作强调
		Span time = new Span(DateUtil.format(schedule.getDtSchedule(), "yyyy-MM-dd HH:mm"));
		time.addClassName("schedule-card-time-pill");
		if (overdue) {
			time.addClassName("schedule-card-time-pill-overdue");
		}
		header.add(time);

		Button deleteBtn = ComFactory.getTertriayBtn("删除", VaadinIcon.TRASH);
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
		deleteBtn.setTooltipText("删除该日程");
		deleteBtn.addClickListener(e -> {
			dialog = ComFactory.getConfirmationDialog("确定删除该日程？", conf -> {
				try {
					scheduleProcess.deleteScheduleById(schedule.getIdUser(), schedule.getIdSchedule());
					initContent();
					ComFactory.notification("删除成功");
				} catch (Exception e1) {
					ComFactory.notificationError("删除失败");
					e1.printStackTrace();
				}
			}, cancel -> {
				dialog.close();
			});
			dialog.open();
		});

		// 右侧竖排（已过期徽标 + 删除按钮）：普通 Div + margin-left:auto 贴卡片右缘。
		// 不用 VerticalLayout——同上，主题会塞 width:100% 把它摊成整行。
		Div rightBox = new Div();
		rightBox.addClassName("schedule-card-side");
		if (overdue) {
			Span badge = new Span("已过期");
			badge.addClassName("schedule-card-overdue-badge");
			rightBox.add(badge);
		}
		rightBox.add(deleteBtn);
		header.add(rightBox);
		card.add(header);

		String comment = schedule.getComment();
		if (null != comment && !comment.isBlank()) {
			// 描述已改存富文本 HTML，卡片引用块只展示纯文本
			String plain = QuillEditor.htmlToPlainText(comment);
			if (!plain.isBlank()) {
				Span body = new Span(plain);
				body.addClassName("schedule-card-comment");
				card.add(body);
			}
		}
		return card;
	}

}
