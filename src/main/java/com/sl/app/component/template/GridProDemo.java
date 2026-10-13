package com.sl.app.component.template;

import java.util.Arrays;
import java.util.List;

import com.sl.app.component.util.ComFactory;
import com.sl.entity.User;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//@PermitAll
//@Route(value = "gridpro") 
public class GridProDemo extends VerticalLayout {
	private static final long serialVersionUID = 8531402836453269364L;

	public GridProDemo() { 
//        addClassName("dashboard-view");
        addClassName("list-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); 
        addGrid();
    }

	private void addGrid() {
		GridPro<User> grid = new GridPro<>();
		grid.setEditOnClick(true);//设置可编辑
        grid.addEditColumn(User::getUserId).text(User::setUserId).setHeader("First name");
        grid.addEditColumn(User::getUserName).text(User::setUserName).setHeader("Last name");
        grid.addEditColumn(User::getEmail).text(User::setEmail).setHeader("Email");
        List<String> membershipOptions = Arrays.asList("Regular", "Premium", "VIP");//使用内置的select 组件进行编辑
        grid.addEditColumn(User::getDepartment).select(User::setDepartment, membershipOptions)
                .setHeader("Membership");
        grid.addEditColumn(User::isFlag).checkbox(User::setFlag).setHeader("冻结状态");//使用内置的checkbox
        grid.addEditColumn(User::getCdPhone).text(User::setCdPhone).setHeader("Profession");
       //由组件组成的列
        grid.addComponentColumn(p ->{
        	Button b = ComFactory.getPrimaryBtn("打印信息");
			b.addClickListener(e -> {
				// 获取已经选择的行
				System.out.println(p.toString());
			});
			return b;
        }).setHeader("自定义");
        grid.addComponentColumn(p ->{
        	ComboBox<String> com = new ComboBox<String>();
        	com.setItems("a","b","c");
        	com.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<?>>() {
				private static final long serialVersionUID = 6299221346864678480L;

				@Override
				public void valueChanged(ValueChangeEvent<?> event) {
					System.out.println(event.getValue());
					System.out.println(event.getOldValue());
				}
			});
        	return com;
        }).setHeader("自定义2");
//        grid.setEditOnClick(true);//单击就可以修改
        List<User> createDemoData = User.createDemoData(10);
        grid.setItems(createDemoData);
        add(grid);
        
    }


}