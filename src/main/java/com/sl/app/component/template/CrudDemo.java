package com.sl.app.component.template;

import java.util.Arrays;
import java.util.List;

import com.sl.app.component.util.UserDataProvider;
import com.sl.entity.User;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CrudDemo extends VerticalLayout {

	private static final long serialVersionUID = 2494481932623034299L;
	private Crud<User> crud;
	private String FIRST_NAME = "userId";
	private String LAST_NAME = "userName";
	private String EMAIL = "email";
	private String PROFESSION = "department";
	private String EDIT_COLUMN = "vaadin-crud-edit-column";

	public CrudDemo() {
		super();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		crud = new Crud<>(User.class, createEditor());
		crud.setHeight("600px");
		setupGrid();
		setupDataProvider();
//		crud.setEditorPosition(CrudEditorPosition.ASIDE);//可以设置不弹窗，靠右侧显示
		add(crud);
	}

	private void setupGrid() {
		Grid<User> grid = crud.getGrid();

		// Only show these columns (all columns shown by default):
		List<String> visibleColumns = Arrays.asList(FIRST_NAME, LAST_NAME, EMAIL, PROFESSION, EDIT_COLUMN);
		grid.getColumns().forEach(column -> {
			String key = column.getKey();
			if (!visibleColumns.contains(key)) {
				grid.removeColumn(column);
			}
		});

		// Reorder the columns (alphabetical by default)
//		grid.setColumnOrder(grid.getColumnByKey(FIRST_NAME), grid.getColumnByKey(LAST_NAME), grid.getColumnByKey(EMAIL),
//				grid.getColumnByKey(PROFESSION), grid.getColumnByKey(EDIT_COLUMN));
	}

	private CrudEditor<User> createEditor() {
		TextField firstName = new TextField("First name");
		TextField lastName = new TextField("Last name");
		EmailField email = new EmailField("Email");
		TextField profession = new TextField("Profession");
		FormLayout form = new FormLayout(firstName, lastName, email, profession);

		Binder<User> binder = new Binder<>(User.class);
		binder.forField(firstName).asRequired().bind(User::getUserId, User::setUserId);
		binder.forField(lastName).asRequired().bind(User::getUserName, User::setUserName);
		binder.forField(email).asRequired().bind(User::getEmail, User::setEmail);
		binder.forField(profession).asRequired().bind(User::getOrganization, User::setOrganization);

		return new BinderCrudEditor<>(binder, form);
	}

	public void setupDataProvider() {
		UserDataProvider dataProvider = new UserDataProvider();
		crud.setDataProvider(dataProvider);
		crud.addDeleteListener(deleteEvent -> dataProvider.delete(deleteEvent.getItem()));
		crud.addSaveListener(saveEvent -> dataProvider.persist(saveEvent.getItem()));
		
//		toolbar
		Html total = new Html("<span>Total: <b>" + dataProvider.DATABASE.size()
        + "</b> employees</span>");

		Button button = new Button("New employee", VaadinIcon.PLUS.create());
		button.addClickListener(event -> {
		    crud.edit(new User(), Crud.EditMode.NEW_ITEM);
		});
		button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		HorizontalLayout toolbar = new HorizontalLayout(total, button);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
		toolbar.setFlexGrow(1, toolbar);
		toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		toolbar.setSpacing(false);
		
		crud.setToolbar(toolbar);
	}

	
}
