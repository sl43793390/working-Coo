package com.sl.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.entity.TaskComment;
import com.sl.entity.User;
import com.sl.service.TaskProcess;
import com.sl.service.UserManageMentProcess;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;

/**
 * 滚动窗口模板
 * 任务留言：
 * avatar+用户名+日期
 * --------------------
 * 留言内容
 * 主要
 * @author Administrator
 *
 */

@SpringComponent
@Scope("prototype")
public class TaskCommentComponent extends VerticalLayout {
	private static final long serialVersionUID = 2490752619011581217L;
	private static final Logger log = LoggerFactory.getLogger(TaskCommentComponent.class);
	private UserManageMentProcess usermanageProcess;
	private TaskProcess taskProcess;
	private String taskId;
	private VerticalLayout mainLayout;
	
	public TaskCommentComponent(@Autowired UserManageMentProcess usermanageProcess,@Autowired TaskProcess taskProcess) {
		this.usermanageProcess = usermanageProcess;
		this.taskProcess = taskProcess;
//		设置外面的大VerticalLayout的高、宽
		setDefaultHorizontalComponentAlignment(Alignment.START);
		setWidth("100%");
		setHeight("770px");
//		设置scroll内部的vetticalLayout的宽度，内部不需要设置高度，随着内容的增多自动增加滚动长度
		mainLayout = ComFactory.getVerticalLayout();
		mainLayout.setWidth("1600px");
		mainLayout.add(ComFactory.getTitle("任务留言列表"));
		mainLayout.add(ComFactory.getHorizontalLine());
//		创建滚动窗口
		Scroller sc = new Scroller(mainLayout);
		sc.setScrollDirection(ScrollDirection.VERTICAL);
		add(sc);//放入大layout中
	}

	/**
	 * scroll 必须放在外面layout里面，否则不起作用
	 */
	public void initContent() {
		List<TaskComment> taskCommentByTaskId = taskProcess.getTaskCommentByTaskId(taskId);
		for (TaskComment taskComment : taskCommentByTaskId) {
			mainLayout.add(generateOne(taskComment));
			TextArea textArea = ComFactory.getTextArea();
			textArea.setWidth("90%");
			textArea.setValue(taskComment.getCdComment());
			textArea.setReadOnly(true);
			mainLayout.add(textArea);
			mainLayout.add(ComFactory.getHorizontalLine());
		}
	}

	private Component generateOne(TaskComment taskComment) {
		HorizontalLayout horizontalLayoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		User selectUserById = usermanageProcess.selectUserById(taskComment.getCommentUser());
		Avatar namedAvatar = ComFactory.getNamedAvatar(selectUserById.getUserName());
		Span user = ComFactory.getLabel(selectUserById.getUserName());
		String formatDateTime = DateUtil.formatDateTime(taskComment.getDtComment());
		Span label = ComFactory.getLabel(formatDateTime);
		horizontalLayoutLeft.add(namedAvatar,user,label);
		return horizontalLayoutLeft;
	}

	public void registerHandler() {

	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	
}
