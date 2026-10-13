package com.sl.app.component.template;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComFactory;
import com.sl.entity.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;

@Service
@Scope("prototype")
public class TreeGridTemplateComponent extends VerticalLayout {

	private static final long serialVersionUID = 4512289455462777038L;

	public TreeGridTemplateComponent() {
		super();
		 setDefaultHorizontalComponentAlignment(Alignment.START); 
		initHeaderLayout();
		initFilterLayout();
		initFilterLayout2();
		initBodyLayout();
	}

	private void initHeaderLayout() {
		add(ComFactory.getTitle("标题示例"));
	}

	private void initFilterLayout() {
		ComboBox<String> com1 = new ComboBox<>();
		com1.setItems("abc","def","wer");
		ComboBox<String> com2 = new ComboBox<>();
		com2.setItems("abc","def","wer");
		ComboBox<String> com3 = new ComboBox<>();
		com3.setItems("abc","def","wer");
		ComboBox<String> com4 = new ComboBox<>();
		com4.setItems("abc","def","wer");
		ComboBox<String> com5 = new ComboBox<>();
		com5.setItems("abc","def","wer");
		HorizontalLayout horizontalLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		horizontalLayout.add(ComFactory.getLabel("标签示例："),com1,ComFactory.getLabel("标签示例："),com2,ComFactory.getLabel("标签示例："),com3,
				ComFactory.getLabel("标签示例："),com4,ComFactory.getLabel("标签示例："),com5);
		add(horizontalLayout);
	}
	private void initFilterLayout2() {
		ComboBox<String> com1 = new ComboBox<>();
		com1.setItems("abc","def","wer");
		ComboBox<String> com2 = new ComboBox<>();
		com2.setItems("abc","def","wer");
		ComboBox<String> com3 = new ComboBox<>();
		com3.setItems("abc","def","wer");
		Button queryBtn = ComFactory.getPrimaryBtn("查询");
		Button resetBtn = ComFactory.getPrimaryBtn("重置全部");
		HorizontalLayout horizontalLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		horizontalLayout.add(ComFactory.getLabel("标签示例："),com1,ComFactory.getLabel("标签示例："),com2,ComFactory.getLabel("标签示例："),com3,
				queryBtn,resetBtn);
		add(horizontalLayout);
	}

	private void initBodyLayout() {
		 TreeGrid<User> grid = new TreeGrid<User>();
		 grid.setSelectionMode(SelectionMode.MULTI);
        Column<User> idHeader = grid.addHierarchyColumn(User::getUserId).setHeader("用户ID");
        Column<User> nameHeader = grid.addColumn(User::getUserName).setHeader("用户名");
        grid.addColumn(User::getCdPhone).setHeader("电话");
        grid.addColumn(User::getEmail).setHeader("邮箱");
        grid.addColumn(User::getOrganization).setHeader("机构");
//        合并表头
        grid.prependHeaderRow().join(idHeader,nameHeader).setText("合并表头");
        User u = new User("userID12","username12","12345678","asdf@163.com","部门1","organization11");
        User u2 = new User("userID1","username1","1234567","asdf@234.com","部门2","organization22");
        User u3 = new User("userID3","username3","1234563","asdf@333.com","部门3","organization33");
        User u4 = new User("userID4","username4","12345674","asdf@444.com","部门4","organization4");
        User u5 = new User("userID5","username5","12345675","asdf@555.com","部门5","organization5");
        TreeData<User> data = new TreeData<>();
        grid.setTreeData(data);
        
        data.addRootItems(u);//每个元素只能add一次
        data.addItem(u, u2);
        grid.expand(u);//展开
        
        data.addRootItems(u3);//添加根节点
        data.addItem(u3, u4);
        data.addItem(u4, u5);
        grid.expand(u3);//展开
        
        add(grid);
		
	}

}
