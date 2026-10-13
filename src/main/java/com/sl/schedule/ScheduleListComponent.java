package com.sl.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.Schedule;
import com.sl.service.ScheduleProcess;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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
		for (int i = 0; i < scheduleByUserId.size(); i++) {
			mainLayout.add(generateCard(scheduleByUserId.get(i)));
		}
	}

	private VerticalLayout generateCard(Schedule schedule) {
		VerticalLayout abs = ComFactory.getVerticalLayout(Alignment.START);
		abs.setHeight("170px");
		// 名称
//		Image img = new Image("img/circle2.png","");
//		img.setHeight("20px");
//		abs.add(img,"left:10px;top:10px");
		HorizontalLayout horizontalLayoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		abs.add(horizontalLayoutLeft);
		Avatar namedAvatar = ComFactory.getNamedAvatar(schedule.getNameSchedule());
		horizontalLayoutLeft.add(namedAvatar);
		Span standardLabel = ComFactory.getLabel(schedule.getNameSchedule());
		horizontalLayoutLeft.add(standardLabel);
		Span label = ComFactory.getLabel(DateUtil.formatDateTime(schedule.getDtSchedule()));
		horizontalLayoutLeft.add(label);
		Button deleteBtn = ComFactory.getTertriayBtn("删除");
		deleteBtn.addClickListener(e ->{
			dialog = ComFactory.getConfirmationDialog("确定删除该日程？", conf ->{
				try {
					scheduleProcess.deleteScheduleById(schedule.getIdUser(), schedule.getIdSchedule());
					initContent();
					ComFactory.notification("删除成功");
				} catch (Exception e1) {
					ComFactory.notificationError("删除失败");
					e1.printStackTrace();
				}
			}, cancel ->{
				dialog.close();
			});
			dialog.open();
		});
		horizontalLayoutLeft.add(deleteBtn);
		TextArea area = ComFactory.getTextArea();
		area.setHeight("80px");
		area.setWidth("90%");
		abs.add(area);
		area.setValue(schedule.getComment());
		area.setReadOnly(true);
		return abs;
	}

}
