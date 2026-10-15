package com.sl.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.Schedule;
import com.sl.service.ScheduleProcess;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;

@SpringComponent
@Scope("prototype")
public class ScheduleListComponent extends VerticalLayout {

	private static final long serialVersionUID = 9071323139692234287L;
	
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
	 * 日程卡片：头部一行排布「头像 + 标题 + 时间 + 删除（右侧竖排）」，
	 * 备注有内容时渲染成引用块。样式见 styles.css 的 .schedule-card 系列。
	 */
	private VerticalLayout generateCard(Schedule schedule) {
		VerticalLayout card = new VerticalLayout();
		card.addClassName("schedule-card");
		card.setPadding(false);
		card.setSpacing(false);
		card.setMargin(false);

		HorizontalLayout header = new HorizontalLayout();
		header.addClassName("schedule-card-header");
		header.setWidthFull();
		header.setAlignItems(Alignment.CENTER);
		header.setSpacing(true);
		header.setMargin(false);
		header.add(ComFactory.getNamedAvatar(schedule.getNameSchedule()));

		Span title = new Span(schedule.getNameSchedule());
		title.addClassName("schedule-card-title");
		header.add(title);

		Icon clock = new Icon(VaadinIcon.CLOCK);
		clock.addClassName("schedule-card-time-icon");
		header.add(clock);

		boolean overdue = null != schedule.getDtSchedule() && schedule.getDtSchedule().before(new Date());

		Span time = new Span(DateUtil.format(schedule.getDtSchedule(), "yyyy-MM-dd HH:mm"));
		time.addClassName("schedule-card-time");
		// 已过期的日程时间标红，一眼区分该办的和拖了的
		if (overdue) {
			time.addClassName("schedule-card-time-overdue");
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

		// 右侧竖排：已过期时徽标悬在删除按钮上方。
		// 靠右用 margin-left:auto（CSS 类），不用占位元素——
		// 主题给所有 HorizontalLayout 的 :host 设了 width:100% + space-between，
		// spacer 的 flex-grow 行为会被 theme 规则搅乱，顶不到卡片右缘（2026-10-15 踩坑）。
		VerticalLayout rightBox = new VerticalLayout();
		rightBox.addClassName("schedule-card-side");
		rightBox.setPadding(false);
		rightBox.setSpacing(false);
		rightBox.setMargin(false);
		rightBox.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
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
			Span body = new Span(comment);
			body.addClassName("schedule-card-comment");
			card.add(body);
		}
		return card;
	}

}
