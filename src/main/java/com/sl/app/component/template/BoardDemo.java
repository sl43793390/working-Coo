package com.sl.app.component.template;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComFactory;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Service
@Scope("prototype")
public class BoardDemo extends VerticalLayout {

	public BoardDemo() {
		testResponsive();

		HorizontalLayout bd = ComFactory.getResponsiveHorizontalLayoutLeft();
		bd.add(ComFactory.getPrimaryBtn("asa"),ComFactory.getPrimaryBtn("ddd"),
				ComFactory.getPrimaryBtn("sss"),ComFactory.getPrimaryBtn("fff"));
		add(bd);
	}

	private void testResponsive() {
		Board board = new Board();
		Row row1 = new Row();// 一行放四个列
		row1.add(ComFactory.getPrimaryBtn("test1"), 1);
		row1.add(new Button("test2"), 1);
		row1.add(ComFactory.getPrimaryBtn("test3"), 1);
		row1.add(new Button("test4"), 1);
		board.addRow(row1);

		Row row2 = new Row();// 一行放两列
		row2.addClassName("back-ground-blue");
		row2.add(new Span("123"), 2);
		row2.add(new Span("123"), 2);
		board.add(row2);

		// 测试一行放三个正常列，一个嵌套列
		Row row3 = new Row();// 一行放四个列
		row3.add(ComFactory.getPrimaryBtn("测试按钮1"), 1);
		row3.add(ComFactory.getPrimaryBtn("测试按钮2"), 1);
		row3.add(ComFactory.getPrimaryBtn("测试按钮3"), 1);
		row3.add(ComFactory.getPrimaryBtn("测试按钮4"), 1);

		HorizontalLayout horizontalLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		Span lb = new Span("demolabel");
		lb.setText("labeltext");
		Button bt2 = ComFactory.getPrimaryBtn("9999");
		Button bu = ComFactory.getPrimaryBtn("确定");
		Button bu3 = ComFactory.getPrimaryBtn("确定3");
		horizontalLayout.add(lb, bt2, bu, bu3);

		Row row5 = new Row();
		row5.add(row3, 3);
		row5.add(horizontalLayout, 1);
		board.add(row5);

		add(board);
		addClassName("basic-board");

	}

}