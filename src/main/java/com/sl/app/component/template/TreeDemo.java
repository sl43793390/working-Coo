package com.sl.app.component.template;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vaadin.tatu.Tree;

import com.sl.entity.User;
import com.vaadin.componentfactory.TreeComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.function.ValueProvider;

@Service
@Scope("prototype")
public class TreeDemo extends VerticalLayout{

	private static final long serialVersionUID = 1979260516644545961L;

	public TreeDemo() {
        
		TreeComboBox<User> treec = new TreeComboBox<User>(getTreeData(),getValueProvider());
		add(treec);
		
		/**
		 * 树结构
		 */
		Tree<User> tr = new Tree<User>(getTreeData(),getValueProvider());
		tr.setItemIconProvider(new ValueProvider<User, VaadinIcon>() {
			//添加图标
			@Override
			public VaadinIcon apply(User source) {
				return VaadinIcon.TASKS;
			}
		});
		tr.setWidth("300px");
		tr.setHeight("500px");
		add(tr);
		
	}

	private ValueProvider<User, String> getValueProvider() {
		ValueProvider<User, String> valueP = new ValueProvider<User, String>() {
			@Override
			public String apply(User source) {
				return source.getUserName();
			}
		};
		return valueP;
	}

	private TreeData<User> getTreeData() {
		User u = new User("userID12","username12","12345678","asdf@163.com","部门1","organization11");
        User u2 = new User("userID1","username1","1234567","asdf@234.com","部门2","organization22");
        User u3 = new User("userID3","username3","1234563","asdf@333.com","部门3","organization33");
        User u4 = new User("userID4","username4","12345674","asdf@444.com","部门4","organization4");
        User u5 = new User("userID5","username5","12345675","asdf@555.com","部门5","organization5");
        TreeData<User> data = new TreeData<>();
        
        data.addRootItems(u);//每个元素只能add一次
        data.addItem(u, u2);
        
        data.addRootItems(u3);//添加根节点
        data.addItem(u3, u4);
        data.addItem(u4, u5);
		return data;
	}
}
