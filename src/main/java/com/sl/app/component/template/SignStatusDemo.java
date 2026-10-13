package com.sl.app.component.template;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * 目前需要结合springMVC解决下载问题，open方法调用springMVC接口进行下载
 * @author Administrator
 *
 */
public class SignStatusDemo extends VerticalLayout{

	private static final long serialVersionUID = 4765719894001360459L;

	public SignStatusDemo() {
		super();
	    addBadges();
	}

	private void addBadges() {
		 // Icon before text
        Span pending1 = new Span(createIcon(VaadinIcon.CLOCK),
                new Span("Pending"));
        pending1.getElement().getThemeList().add("badge");

        Span confirmed1 = new Span(createIcon(VaadinIcon.CHECK),
                new Span("Confirmed"));
        confirmed1.getElement().getThemeList().add("badge success");

        Span denied1 = new Span(createIcon(VaadinIcon.EXCLAMATION_CIRCLE_O),
                new Span("Denied"));
        denied1.getElement().getThemeList().add("badge error");

        Span onHold1 = new Span(createIcon(VaadinIcon.HAND),
                new Span("On hold"));
        onHold1.getElement().getThemeList().add("badge contrast");

        // Icon after text
        Span pending2 = new Span(new Span("Pending"),
                createIcon(VaadinIcon.CLOCK));
        pending2.getElement().getThemeList().add("badge");

        Span confirmed2 = new Span(new Span("Confirmed"),
                createIcon(VaadinIcon.CHECK));
        confirmed2.getElement().getThemeList().add("badge success");

        Span denied2 = new Span(new Span("Denied"),
                createIcon(VaadinIcon.EXCLAMATION_CIRCLE_O));
        denied2.getElement().getThemeList().add("badge error");

        Span onHold2 = new Span(new Span("On hold"),
                createIcon(VaadinIcon.HAND));
        onHold2.getElement().getThemeList().add("badge contrast");

        add(new HorizontalLayout(pending1, confirmed1, denied1, onHold1),
                new HorizontalLayout(pending2, confirmed2, denied2, onHold2));
        setPadding(false);
        setSizeUndefined();
        // 图标
        Icon confirmed = createIcon(VaadinIcon.CHECK, "Confirmed");
        confirmed.getElement().getThemeList().add("badge success");

        Icon cancelled = createIcon(VaadinIcon.CLOSE_SMALL, "Cancelled");
        cancelled.getElement().getThemeList().add("badge error");

        add(confirmed, cancelled);
        //圆形
        Span pending = new Span("Pending");
        pending.getElement().getThemeList().add("badge pill");

        Span confirmed3 = new Span("Confirmed");
        confirmed3.getElement().getThemeList().add("badge success pill");

        Span denied = new Span("Denied");
        denied.getElement().getThemeList().add("badge error pill");

        Span onHold = new Span("On hold");
        onHold.getElement().getThemeList().add("badge contrast pill");

        add(pending, confirmed3, denied, onHold);
        
        messageListComponent();
        messageInputComponent();
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
    

    private Icon createIcon(VaadinIcon vaadinIcon, String label) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", label);
        // Tooltip
        icon.getElement().setAttribute("title", label);
        return icon;
    }
		
    public void messageListComponent() {
        MessageList list = new MessageList();
        Instant yesterday = Instant.now();
        Instant fiftyMinsAgo = LocalDateTime.now().minusMinutes(50)
                .toInstant(ZoneOffset.UTC);
        MessageListItem message1 = new MessageListItem(
                "Linsey, could you check if the details with the order are okay?",
                yesterday, "Matt Mambo");
        message1.setUserColorIndex(1);
        MessageListItem message2 = new MessageListItem("All good. Ship it.",
                fiftyMinsAgo, "Linsey Listy", "img/2-home-page.png");
        message2.setUserColorIndex(2);
        list.setItems(Arrays.asList(message1, message2));
        add(list);
    }
    
    public void messageInputComponent() {
        MessageInput input = new MessageInput();
        input.addSubmitListener(submitEvent -> {
            Notification.show("Message received: " + submitEvent.getValue(),
                    3000, Notification.Position.MIDDLE);
        });
        add(input);
    }
}
