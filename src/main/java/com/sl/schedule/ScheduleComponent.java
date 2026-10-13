package com.sl.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.stefan.fullcalendar.CalendarLocale;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.entity.Schedule;
import com.sl.entity.User;
import com.sl.service.ScheduleProcess;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import elemental.json.Json;
import elemental.json.JsonObject;

/**
 * 我的日程（T2-11）。
 * <p>
 * 用开源插件 <a href="https://github.com/stefanuebe/vaadin-fullcalendar">FullCalendar for Flow</a>
 * （封装 FullCalendar 6.x，MIT）替换了旧的手写日历。替代前存在的问题：
 * 月/周视图格子是固定 13% 宽的 HorizontalLayout，日程一多就互相重叠溢出；
 * 拖拽改期落点写死 9:00（拖到下午也会变成早上 9 点）；日视图只按起始小时分桶，时长不可视。
 * <p>
 * 现在的能力：
 * <ol>
 *   <li><b>月 / 周 / 日 / 列表四种视图</b>，工具栏由 FullCalendar 自带（前进后退、今天、视图切换、中文 locale）；</li>
 *   <li><b>拖拽改期</b>：日程块拖到任何位置，时间精确跟随落点（修复旧版写死 9:00 的 bug），时长不变，
 *       改完 FLAG_REMINDED 重置为 N，提醒按新时间重发；</li>
 *   <li><b>拖拽边缘改时长</b>（旧版没有）：日程块右下/下边缘可拉伸；</li>
 *   <li><b>点击空白新建</b>：日/周视图点时间格预填该时刻，月视图点日期格预填全天；
 *       也可以按住拖选一个时间段，起止一起预填；</li>
 *   <li><b>提醒进通知中心</b>：新建时可设「提前多少分钟」，由 NotificationScheduler 每 5 分钟扫描一次写入
 *       notification 表，和任务到期提醒共用同一个铃铛。</li>
 * </ol>
 */
@SpringComponent
@Scope("prototype")
public class ScheduleComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = -8114879099198406371L;

	private static final Logger log = LoggerFactory.getLogger(ScheduleComponent.class);

	/** 旧版日程块的主色，保留沿用 */
	private static final String COLOR_TIMED = "#1676f3";
	private static final String COLOR_ALLDAY = "#0f9d58";

	/** 描述最大长度，与 schedule 表 COMMENT 列宽保持一致（当前 varchar(2000)），列扩宽后同步调大 */
	private static final int COMMENT_MAX_LENGTH = 2000;

	/** 待办清单里的时间格式：跨天日程要显示完整起止，不能像 FC list 视图那样只剩 00:00:00 */
	private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final ScheduleProcess scheduleProcess;

	private final FullCalendar calendar = new FullCalendar();
	/** 日程数据源：每次 reload 全量替换 */
	private final InMemoryEntryProvider<Entry> entryProvider = EntryProvider.emptyInMemory();

	/** 待办清单位：和日历同一个位置切换显示，替代 FC 的 listWeek 视图 */
	private final VerticalLayout todoHolder = new VerticalLayout();
	/** true=显示待办清单，false=显示日历 */
	private boolean listMode = false;
	private Button viewToggleBtn;

	private List<Schedule> schedules = new ArrayList<>();
	private List<User> projectUsers = new ArrayList<>();

	/** 新建弹窗引用：TimeslotClicked 和 TimeslotsSelected 可能在一次操作里先后都触发，用它防双开 */
	private Dialog dialog;

	public ScheduleComponent(@Autowired ScheduleProcess scheduleProcess) {
		this.scheduleProcess = scheduleProcess;
		setMargin(false);
		setDefaultHorizontalComponentAlignment(Alignment.START);
		setSizeFull();
		add(ComFactory.getTitle("我的日程表"));
		add(ComFactory.getHorizontalLine());
		initToolbar();

		initCalendar();
		add(calendar);
		todoHolder.setPadding(false);
		todoHolder.setWidthFull();
		todoHolder.setVisible(false);
		add(todoHolder);
		reload();
	}

	// ------------------------------------------------------------------ 初始化

	private void initToolbar() {
		HorizontalLayout bar = ComFactory.getResponsiveHorizontalLayoutLeft();
		bar.setWidthFull();
		bar.setAlignItems(Alignment.CENTER);

		// .vaadin-button-back 全局写死 90px 宽，按钮文字超过两个字就会被截断，这里放开
		Button addBtn = ComFactory.getPrimaryBtn("新建日程", VaadinIcon.PLUS);
		addBtn.setWidth("auto");
		addBtn.addClickListener(e -> openDialog(null, LocalDateTime.now(), null, false));

		Span hint = new Span("点击空白格或拖选时间段可新建；拖动日程块改期；拖拽边缘调时长");
		hint.getStyle().set("color", "#888").set("font-size", "small");
		// 占满剩余空间，把右侧的视图切换按钮推到最右
		hint.getStyle().set("flex-grow", "1");

		// 「待办清单 / 返回日历」切换。FC 自带的 listWeek 有两个问题：只看一周，
		// 且跨天事件会按天拆成多行、中间日只剩 allDayText，看不到完整起止时间，
		// 所以清单改成自研面板（buildTodoList）
		viewToggleBtn = ComFactory.getPrimaryBtn("待办清单", VaadinIcon.LIST);
		viewToggleBtn.setWidth("auto");
		viewToggleBtn.addClickListener(e -> toggleView());

		bar.add(addBtn, hint, viewToggleBtn);
		add(bar);
	}

	private void initCalendar() {
		calendar.setEntryProvider(entryProvider);
		// 作用域类名：styles.css 里 .schedule-calendar 下的 FC 样式只影响日程页
		calendar.addClassName("schedule-calendar");
		calendar.setHeight("700px");
		calendar.setWidth("1300px");
		calendar.setLocale(CalendarLocale.CHINESE.getLocale());
		calendar.setFirstDay(DayOfWeek.MONDAY);

		// 顶部工具栏：翻页 + 标题 + 视图切换，全由 FullCalendar 自带
		// 注意：不放 listWeek——跨天日程在 FC list 视图里会按天拆行、时间显示不全，
		// 清单入口换成工具栏上的「待办清单」按钮（自研面板）
		JsonObject toolbar = Json.createObject();
		toolbar.put("start", "prev,next today");
		toolbar.put("center", "title");
		toolbar.put("end", "dayGridMonth,timeGridWeek,timeGridDay");
		calendar.setOption("headerToolbar", toolbar);

		// 拖拽 / 缩放 / 框选
		calendar.setEditable(true);
		calendar.setEntryDurationEditable(true);
		calendar.setTimeslotsSelectable(true);
		calendar.setNowIndicatorShown(true);

		// 月视图一天最多画 4 条，超出折叠成「+N 更多」弹层，避免撑爆格子
		calendar.setMaxEntriesPerDay(4);
		calendar.setMoreLinkClickAction(FullCalendar.MoreLinkClickAction.POPUP);

		// 日/周视图从 7 点开始画，滚动定位到 8 点，别从 0 点开始看一屏空白
		calendar.setSlotMinTime(LocalTime.of(7, 0));
		calendar.setOption("scrollTime", "08:00:00");

		// 鼠标悬停显示日程描述（customProperties 会原样进 extendedProps）
		calendar.setEntryDidMountCallback("function(info){var p=(info.event.extendedProps||{});"
				+ "var c=p.comment||(p.customProperties&&p.customProperties.comment)||'';"
				+ "if(c){info.el.title=c;}}");

		registerCalendarListeners();
	}

	// ------------------------------------------------------------------ 事件

	private void registerCalendarListeners() {
		// 点日程块 → 编辑
		calendar.addEntryClickedListener(e -> {
			Schedule s = byId(e.getEntry().getId());
			if (null != s) {
				openDialog(s, null, null, false);
			}
		});

		// 单击空白格 → 新建（预填该时刻 / 该天）
		calendar.addTimeslotClickedListener(e -> {
			if (isDialogOpen()) {
				return;
			}
			openDialog(null, e.getDateTime(), null, e.isAllDay());
		});

		// 拖选一段 → 新建（预填起止）
		calendar.addTimeslotsSelectedListener(e -> {
			if (isDialogOpen()) {
				return;
			}
			openDialog(null, e.getStart(), e.getEnd(), e.isAllDay());
		});

		// 拖拽日程块 → 改期（时长不变，FC 已在客户端算好新的起止）
		calendar.addEntryDroppedListener(e -> {
			Entry entry = e.applyChangesOnEntry();
			Schedule s = byId(entry.getId());
			if (null == s) {
				return;
			}
			try {
				int n = scheduleProcess.reschedule(s, Util.locaDateTimeToDate(entry.getStart()));
				if (n > 0) {
					ComFactory.notificationSuccess("已改期到 " + entry.getStart().toLocalDate());
					refreshCalendarData();
				} else {
					ComFactory.notificationError("改期失败，请刷新后重试");
				}
			} catch (Exception ex) {
				log.error(ExceptionUtils.getStackTrace(ex));
				ComFactory.notificationError("改期失败：" + ex.getMessage());
			}
		});

		// 拖拽日程块边缘 → 改时长（只动结束时间，不动提醒标记）
		calendar.addEntryResizedListener(e -> {
			Entry entry = e.applyChangesOnEntry();
			if (null == entry.getEnd()) {
				return;
			}
			try {
				// 基于最新行更新，别用页面加载快照——否则可能把调度器刚写入的 FLAG_REMINDED=Y 覆盖回 N
				Schedule snapshot = byId(entry.getId());
				Schedule s = null == snapshot ? null
						: scheduleProcess.getScheduleById(snapshot.getIdUser(), entry.getId());
				if (null == s) {
					ComFactory.notificationError("日程已被删除，请刷新");
					refreshCalendarData();
					return;
				}
				if (entry.isAllDay()) {
					// FC 全天日程的 end 是排他边界（显示到 end 前一天），DB 里存「最后一天」
					s.setDtEnd(Util.locaDateToDate(entry.getEnd().toLocalDate().minusDays(1)));
				} else {
					s.setDtEnd(Util.locaDateTimeToDate(entry.getEnd()));
				}
				int n = scheduleProcess.updateSchedule(s);
				if (n > 0) {
					ComFactory.notificationSuccess("时长已更新");
					refreshCalendarData();
				} else {
					ComFactory.notificationError("更新失败，请刷新后重试");
				}
			} catch (Exception ex) {
				log.error(ExceptionUtils.getStackTrace(ex));
				ComFactory.notificationError("更新失败：" + ex.getMessage());
			}
		});
	}

	private boolean isDialogOpen() {
		return null != dialog && dialog.isOpened();
	}

	private Schedule byId(String idSchedule) {
		return schedules.stream()
				.filter(s -> null != idSchedule && idSchedule.equals(s.getIdSchedule()))
				.findFirst().orElse(null);
	}

	// ------------------------------------------------------------------ 数据

	@Override
	public void refresh() {
		reload();
	}

	private void reload() {
		String me = ComponentUtil.getCurrentUser() == null ? null : ComponentUtil.getCurrentUser().getUserId();
		if (null == me) {
			ComFactory.notificationError("登录状态已失效，请重新登录");
			schedules = new ArrayList<>();
		} else {
			schedules = scheduleProcess.getScheduleByUserId(me);
		}
		try {
			projectUsers = scheduleProcess.getProjectUserList();
		} catch (Exception e) {
			// 没选项目时 getProjectUserList 会 NPE，这时候只是加不了参与人，不影响看日程
			log.warn("取项目成员失败，日程参与人下拉为空", e);
			projectUsers = new ArrayList<>();
		}
		refreshCalendarData();
	}

	/**
	 * 把当前 schedules 全量刷进日历（FC 会自动按视图区间向 provider 拉取）。
	 * <p>
	 * 注意：InMemoryEntryProvider 的 addEntries/removeEntries 只改内部 map，
	 * <b>不会</b>触发 entriesChange 事件；必须显式调 refreshAll() 通知客户端
	 * refetch，否则新建/改期后客户端不刷新，要整页重开才看得到。
	 */
	private void refreshCalendarData() {
		entryProvider.removeAllEntries();
		entryProvider.addEntries(schedules.stream().map(this::toEntry).collect(Collectors.toList()));
		entryProvider.refreshAll();
	}

	// ------------------------------------------------------------------ 待办清单

	/** 日历 <-> 待办清单 切换 */
	private void toggleView() {
		listMode = !listMode;
		calendar.setVisible(!listMode);
		todoHolder.setVisible(listMode);
		if (listMode) {
			viewToggleBtn.setText("返回日历");
			viewToggleBtn.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
			buildTodoList();
		} else {
			viewToggleBtn.setText("待办清单");
			viewToggleBtn.setIcon(new Icon(VaadinIcon.LIST));
		}
	}

	/**
	 * 自研待办清单：全量渲染当前用户的日程，不过滤周范围。
	 * <p>
	 * 没用 FC 的 listWeek 视图，两个原因：① 它只列一周，看不到全部待办；
	 * ② 跨天事件会被 FC 按天拆成多行，中间日只剩 allDayText，起止时间显示不全。
	 * 这里每条一行，完整给出起止时间，跨天显式标注共 N 天。
	 */
	private void buildTodoList() {
		todoHolder.removeAll();
		if (schedules.isEmpty()) {
			Span empty = new Span("暂无待办日程，点击「新建日程」或点击日历空白格创建");
			empty.addClassName("todo-empty");
			todoHolder.add(empty);
			return;
		}
		// 未过期的按开始时间升序（越近越靠前）；已过期的按开始降序排在后面并置灰
		List<Schedule> upcoming = schedules.stream().filter(s -> !isPast(s))
				.sorted(Comparator.comparing(Schedule::getDtSchedule))
				.collect(Collectors.toList());
		List<Schedule> past = schedules.stream().filter(s -> isPast(s))
				.sorted(Comparator.comparing(Schedule::getDtSchedule).reversed())
				.collect(Collectors.toList());
		if (!upcoming.isEmpty()) {
			todoHolder.add(sectionTitle("待完成（" + upcoming.size() + "）"));
			upcoming.forEach(s -> todoHolder.add(todoItem(s, false)));
		}
		if (!past.isEmpty()) {
			todoHolder.add(sectionTitle("已过期（" + past.size() + "）"));
			past.forEach(s -> todoHolder.add(todoItem(s, true)));
		}
	}

	/** 已过期 = 结束时间（没有结束时间按开始时间算）早于现在 */
	private boolean isPast(Schedule s) {
		LocalDateTime end = null == s.getDtEnd()
				? Util.dateToLocalDateTime(s.getDtSchedule())
				: Util.dateToLocalDateTime(s.getDtEnd());
		return end.isBefore(LocalDateTime.now());
	}

	private Span sectionTitle(String text) {
		Span t = new Span(text);
		t.addClassName("todo-section");
		return t;
	}

	/** 单条待办：色点 + 标题 + 完整时间 + 描述（截断），整行可点，点击进编辑弹窗 */
	private HorizontalLayout todoItem(Schedule s, boolean past) {
		HorizontalLayout item = new HorizontalLayout();
		item.addClassName("todo-item");
		// 主题组件 CSS 给所有 layout 设了 justify-content: space-between，会把色点和
		// 内容推到两端，这里显式改回左对齐（同 .kanban-board 的处理）
		item.getStyle().set("justify-content", "flex-start");
		item.getStyle().set("cursor", "pointer");
		if (past) {
			item.addClassName("todo-item-past");
		}
		item.setAlignItems(Alignment.CENTER);
		item.setWidthFull();

		Span dot = new Span();
		dot.addClassName("todo-dot");
		dot.addClassName("全天".equals(s.getCdNature()) ? "todo-dot-allday" : "todo-dot-timed");

		VerticalLayout main = new VerticalLayout();
		main.setPadding(false);
		main.setSpacing(false);
		main.getStyle().set("flex-grow", "1");
		Span title = new Span(StrUtil.nullToDefault(s.getNameSchedule(), "(无标题)"));
		title.addClassName("todo-title");
		Span time = new Span(timeText(s));
		time.addClassName("todo-time");
		main.add(title, time);
		if (StrUtil.isNotBlank(s.getComment())) {
			String c = s.getComment();
			Span desc = new Span(c.length() > 60 ? c.substring(0, 60) + "…" : c);
			desc.addClassName("todo-desc");
			desc.setTitle(c);
			main.add(desc);
		}
		item.add(dot, main);
		item.addClickListener(e -> openDialog(s, null, null, false));
		return item;
	}

	/**
	 * 完整起止时间。跨天日程显式标注共 N 天：
	 * DB 里 DT_END 存「最后一天（含）」，所以天数 = 起止相隔 + 1。
	 */
	private String timeText(Schedule s) {
		LocalDate startDay = Util.dateToLocalDate(s.getDtSchedule());
		boolean allDay = "全天".equals(s.getCdNature()) || null == s.getDtEnd();
		LocalDate endDay = null == s.getDtEnd() ? startDay : Util.dateToLocalDate(s.getDtEnd());
		long days = ChronoUnit.DAYS.between(startDay, endDay) + 1;
		String range = allDay
				? startDay.toString()
				: Util.dateToLocalDateTime(s.getDtSchedule()).format(TIME_FMT)
						+ " ~ " + Util.dateToLocalDateTime(s.getDtEnd()).format(TIME_FMT);
		if (days > 1) {
			range += "（共 " + days + " 天）";
		}
		return range;
	}

	/**
	 * Schedule → FC Entry。与旧版口径一致：CD_NATURE 为「全天」或没有结束时间的都按全天日程处理。
	 * <p>
	 * 注意：Entry 的 end 绝对不能为 null。客户端 refetch 时服务端用 EntryQuery 过滤，
	 * 而 {@code applyFilter} 里 {@code e.getEnd() != null} 是硬条件——end 为空的条目
	 * 会被直接丢弃，日历上永远不显示（且只在动态刷新时暴露，极易误判）。
	 */
	private Entry toEntry(Schedule s) {
		Entry e = new Entry(s.getIdSchedule());
		e.setTitle(StrUtil.nullToDefault(s.getNameSchedule(), "(无标题)"));
		if ("全天".equals(s.getCdNature()) || null == s.getDtEnd()) {
			e.setAllDay(true);
			LocalDate startDay = Util.dateToLocalDate(s.getDtSchedule());
			e.setStart(startDay.atStartOfDay());
			// DB 的 DT_END 存「最后一天（含）」；为空时视为单天。FC 的 end 是排他边界，+1 天
			LocalDate lastDay = null == s.getDtEnd() ? startDay : Util.dateToLocalDate(s.getDtEnd());
			e.setEnd(lastDay.plusDays(1).atStartOfDay());
			e.setColor(COLOR_ALLDAY);
		} else {
			e.setStart(Util.dateToLocalDateTime(s.getDtSchedule()));
			e.setEnd(Util.dateToLocalDateTime(s.getDtEnd()));
			e.setColor(COLOR_TIMED);
		}
		e.setEditable(true);
		if (StrUtil.isNotBlank(s.getComment())) {
			e.setCustomProperties(Map.of("comment", s.getComment()));
		}
		return e;
	}

	// ------------------------------------------------------------------ 弹窗

	/**
	 * 新建 / 编辑日程。schedule 为 null 是新建；presetStart 非空时按它预填起始时间，
	 * presetAllDay 表示来源是全天格（月视图日期格 / 全天行）；presetEnd 非空时预填结束
	 * （拖选时间段 / 多天时为排他边界，处理时已减回「最后一天」）。
	 */
	private void openDialog(Schedule schedule, LocalDateTime presetStart, LocalDateTime presetEnd, boolean presetAllDay) {
		boolean edit = null != schedule;
		dialog = new Dialog();
		dialog.setWidth("680px");
		dialog.setCloseOnOutsideClick(false);

		VerticalLayout body = new VerticalLayout();
		body.setHeight("520px");
		body.setWidth("620px");

		FormLayout form = new FormLayout();
		Checkbox allday = ComFactory.getCheckbox("全天");
		DateTimePicker dtStart = new DateTimePicker("日程起始：");
		DateTimePicker dtEnd = new DateTimePicker("日程结束：");
		dtStart.setWidth("380px");
		dtEnd.setWidth("380px");
		TextField title = new TextField("日程标题：");
		title.setWidth("380px");
		TextArea area = new TextArea("日程描述：");
		area.setWidth("380px");
		area.setMaxLength(COMMENT_MAX_LENGTH);
		ComboBox<Integer> remindBox = ComFactory.getCombobox(Integer.class);
		remindBox.setItems(0, 5, 10, 15, 30, 60, 120, 1440);
		remindBox.setItemLabelGenerator(m -> 0 == m ? "不提醒" : (m >= 1440 ? "提前 1 天" : "提前 " + m + " 分钟"));
		remindBox.setValue(0);
		form.add(allday, dtStart, dtEnd, title, area, remindBox);
		body.add(form);

		// 全天 ↔ 时段 联动：勾「全天」时结束时间表示「结束日期（含）」，默认当天；
		// 取消勾选时恢复成日期时间，结束默认为开始 +1 小时
		allday.addValueChangeListener(ev -> {
			LocalDateTime start = dtStart.getValue();
			LocalDateTime end = dtEnd.getValue();
			if (Boolean.TRUE.equals(ev.getValue())) {
				if (null != start && (null == end || end.toLocalDate().isBefore(start.toLocalDate()))) {
					dtEnd.setValue(start.toLocalDate().atStartOfDay());
				}
			} else if (null != start && (null == end || !end.isAfter(start))) {
				dtEnd.setValue(start.plusHours(1));
			}
		});

		MultiSelectComboBox<User> userBox = ComFactory.getComboboxMultiSelect("参与人：", User.class);
		userBox.setItemLabelGenerator(User::getUserName);
		userBox.setItems(projectUsers);
		userBox.setWidth("380px");
		body.add(userBox);

		if (edit) {
			boolean isAllDay = "全天".equals(schedule.getCdNature());
			allday.setValue(isAllDay);
			LocalDate startDay = Util.dateToLocalDate(schedule.getDtSchedule());
			if (isAllDay) {
				// 全天日程 DB 只存日期（00:00），回显成 09:00 更符合直觉，保存时只取日期部分
				dtStart.setValue(startDay.atTime(9, 0));
				LocalDate lastDay = null == schedule.getDtEnd() ? startDay : Util.dateToLocalDate(schedule.getDtEnd());
				dtEnd.setValue(lastDay.atStartOfDay());
			} else {
				dtStart.setValue(Util.dateToLocalDateTime(schedule.getDtSchedule()));
				if (null != schedule.getDtEnd()) {
					dtEnd.setValue(Util.dateToLocalDateTime(schedule.getDtEnd()));
				}
			}
			title.setValue(StrUtil.nullToDefault(schedule.getNameSchedule(), ""));
			area.setValue(StrUtil.nullToDefault(schedule.getComment(), ""));
			remindBox.setValue(null == schedule.getNbrRemindMinutes() ? 0 : schedule.getNbrRemindMinutes());
			String participants = schedule.getParticipatingUser();
			if (null != participants && !"all".equals(participants) && !participants.isBlank()) {
				Set<String> ids = Set.of(participants.split(","));
				userBox.setValue(projectUsers.stream().filter(u -> ids.contains(u.getUserId()))
						.collect(java.util.stream.Collectors.toSet()));
			}
			Button delBtn = new Button("删除日程", new Icon(VaadinIcon.TRASH));
			delBtn.addClickListener(e -> {
				scheduleProcess.deleteScheduleById(schedule.getIdUser(), schedule.getIdSchedule());
				ComFactory.notificationSuccess("已删除");
				dialog.close();
				reload();
			});
			body.add(delBtn);
		} else if (null != presetStart) {
			// 拖选多天时 presetEnd 是排他边界（最后一天的下一天），减回来才是「最后一天（含）」
			LocalDate presetEndDay = null == presetEnd ? null : presetEnd.toLocalDate().minusDays(1);
			allday.setValue(presetAllDay);
			if (presetAllDay) {
				dtStart.setValue(presetStart.toLocalDate().atTime(9, 0));
				dtEnd.setValue((null == presetEndDay ? presetStart.toLocalDate() : presetEndDay).atStartOfDay());
			} else {
				dtStart.setValue(presetStart);
				dtEnd.setValue(null == presetEnd ? presetStart.plusHours(1) : presetEnd);
			}
		}

		Button saveBtn = ComFactory.getPrimaryBtn("保存");
		Button cancelBtn = ComFactory.getPrimaryBtn("取消");
		saveBtn.addClickListener(e -> {
			LocalDateTime start = dtStart.getValue();
			LocalDateTime end = dtEnd.getValue();
			if (!allday.getValue()) {
				if (null == start || null == end) {
					ComFactory.notification("请先选择起始和结束时间");
					return;
				}
				if (end.isBefore(start)) {
					ComFactory.notification("结束时间不能早于起始时间");
					return;
				}
			} else {
				if (null == start) {
					ComFactory.notification("全天日程也要选一个起始日期");
					return;
				}
				if (null == end || end.toLocalDate().isBefore(start.toLocalDate())) {
					ComFactory.notification("结束日期不能早于起始日期");
					return;
				}
			}
			Schedule sc = edit ? schedule : new Schedule();
			if (!edit) {
				sc.setIdSchedule(IdUtil.getSnowflakeNextIdStr());
				sc.setIdUser(ComponentUtil.getCurrentUser().getUserId());
			}
			if (allday.getValue()) {
				// 全天：DT_END 存「最后一天（含）」，与旧数据口径一致；FC 渲染时再 +1 天转排他边界
				sc.setCdNature("全天");
				sc.setDtSchedule(Util.locaDateToDate(start.toLocalDate()));
				sc.setDtEnd(Util.locaDateToDate(end.toLocalDate()));
			} else {
				sc.setCdNature(null);
				sc.setDtSchedule(Util.locaDateTimeToDate(start));
				sc.setDtEnd(Util.locaDateTimeToDate(end));
			}
			sc.setNameSchedule(title.getValue());
			sc.setComment(area.getValue());
			Integer remind = remindBox.getValue() == null ? 0 : remindBox.getValue();
			sc.setNbrRemindMinutes(remind > 0 ? remind : null);
			Set<User> users = userBox.getValue();
			if (null != users && !users.isEmpty()) {
				StringBuilder bf = new StringBuilder();
				users.forEach(u -> bf.append(u.getUserId()).append(","));
				sc.setParticipatingUser(StrUtil.removeSuffix(bf.toString(), ","));
			} else {
				sc.setParticipatingUser("all");// 代表全部用户
			}
			try {
				if (edit) {
					sc.setFlagReminded("N");// 改了时间就重新提醒一次
					scheduleProcess.updateSchedule(sc);
				} else {
					scheduleProcess.saveSchedule(sc);
				}
				// 跳到日程所在月/周，让用户立刻看到刚存的这条
				calendar.gotoDate(Util.dateToLocalDate(sc.getDtSchedule()));
				ComFactory.notificationSuccess("保存成功");
			} catch (Exception ex) {
				log.error(ExceptionUtils.getStackTrace(ex));
				ComFactory.notificationError("保存失败：" + ex.getMessage());
				return;
			}
			dialog.close();
			reload();
		});
		cancelBtn.addClickListener(e -> dialog.close());

		dialog.setHeaderTitle(edit ? "编辑日程" : "新建日程");
		dialog.add(body);
		dialog.getFooter().add(cancelBtn, saveBtn);
		dialog.open();
	}

	public void initContent() {
		// 保留旧调用签名，实际渲染走 reload()
		reload();
	}

	public void registerHandler() {
		// 交互都在各自的按钮 / 日历事件上注册，这里留空以兼容 ComponentUtil.initComponent 的调用顺序
	}
}
