package com.sl.app.component.template;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.CancelEvent;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class DialogDemo extends VerticalLayout {

	private static final long serialVersionUID = 5457691147760273449L;

	public DialogDemo() {
		super();

		Dialog dialog = createDialog();
		Dialog dialog2 = createDialogAddUser();
		ConfirmDialog confirmationDialog = confirmationDialog(e -> System.out.println("确认"), e1 ->System.out.println("取消"));

		Button button = new Button("显示对话框", e -> dialog.open());
		Button button2 = new Button("显示对话框", e -> dialog2.open());
		 Button buttonConfirmation = new Button("打开确认对话框");
		 buttonConfirmation.addClickListener(event -> { confirmationDialog.open(); });

		add(button,button2,buttonConfirmation);
	}
	/**
	 * 带有内容的对话框
	 * @return
	 */
	private Dialog createDialog() {
		Dialog dialog = new Dialog();

		dialog.setCloseOnOutsideClick(false);
//		dialog.setWidth("600px");
		dialog.setHeaderTitle("添加信息");

		VerticalLayout dialogLayout = createDialogLayout();
		dialog.add(dialogLayout);

		Button saveButton = new Button("保存", e -> dialog.close());
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		Button cancelButton = new Button("取消", e -> dialog.close());
		dialog.getFooter().add(cancelButton);
		dialog.getFooter().add(saveButton);
		return dialog;
	}
	
	private static VerticalLayout createDialogLayout() {

		TextField firstNameField = new TextField("用户名");
		TextField lastNameField = new TextField("密码");

		VerticalLayout dialogLayout = new VerticalLayout(firstNameField, lastNameField);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

		return dialogLayout;
	}
	
	private Dialog createDialogAddUser() {
		Dialog dialog = new Dialog();
		dialog.setResizable(true);
		dialog.setMinWidth("300px");
		dialog.setCloseOnOutsideClick(true);
		dialog.setMaxWidth("500px");
		dialog.setMaxHeight("600px");
		dialog.setHeaderTitle("添加成员");
		
		FormLayout dialogLayout = createDialogLayoutUser();
		dialog.add(dialogLayout);
		
		Button saveButton = new Button("保存", e -> dialog.close());
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		Button cancelButton = new Button("取消", e -> dialog.close());
		Button loadButton = new Button("批量导入", e -> dialog.close());
		dialog.getFooter().add(cancelButton);
		dialog.getFooter().add(saveButton);
		dialog.getFooter().add(loadButton);
		return dialog;
	}

	private static FormLayout createDialogLayoutUser() {
		FormLayout form = new FormLayout();
		ResponsiveStep step = new ResponsiveStep("300px", 1, LabelsPosition.ASIDE);
		form.setResponsiveSteps(step);
		TextField textField = new TextField("用户名");
		textField.setRequired(true);
		textField.setClearButtonVisible(true);
		textField.setErrorMessage("必填");
		form.add(textField);
		form.add(new TextField("姓名"));
		form.add(new EmailField("邮箱"));
		form.add(new TextField("手机号"));
		form.add(new PasswordField("密码"));
		form.add(new NumberField("员工编号"));
		form.add(new TextField("部门"));
		form.add(new TextField("职位"));
		return form;
	}
	/**
	 * 确认对话框
	 * @return 
	 */
	private ConfirmDialog confirmationDialog(ComponentEventListener<ConfirmEvent> confirmListener,ComponentEventListener<CancelEvent> cancelListener) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("提示");
        dialog.setText("请确认是否要删除所选内容？");

        dialog.setCancelable(true);
        dialog.setCancelButton("取消", cancelListener);
        
        dialog.setConfirmText("确认");
        dialog.setConfirmButton("确认", confirmListener);
        return dialog;
	}

}
