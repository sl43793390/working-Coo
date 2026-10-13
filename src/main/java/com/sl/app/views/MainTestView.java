package com.sl.app.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
@PageTitle("Main")
@Route(value = "main")
public class MainTestView extends VerticalLayout {

    private TextField name;
    private Button sayHello;
    private Button logout;

    public MainTestView() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        logout = new Button("logout");
        sayHello.addClickListener(e ->Notification.show("Hello " + name.getValue()));
//        logout.addClickListener(e -> logOut());
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setDefaultHorizontalComponentAlignment(Alignment.START);
//        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello,logout);
    }

//    @Secured("admin122")
//	private void logOut() {
//		AuthenticatedUser bean = ComponentUtil.applicationContext.getBean(AuthenticatedUser.class);
//		bean.logout();
//	}

}
