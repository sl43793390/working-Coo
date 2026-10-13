package com.sl.app.views;

import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

/**
 * Code snippets used in CollaborationMessageList's reference documentation.
 */
@Route("charview")
public class MessageListDocumentation extends VerticalLayout {

	private AtomicInteger intger = new AtomicInteger();
	private static final long serialVersionUID = 5015961349300113391L;
	private CollaborationMessageList messageList;
    private UserInfo userInfo = new UserInfo("test", "test", "./user.png");
    private String topicId = "general";

//    private UserService userService;

//    private MessageService messageService;

    public MessageListDocumentation() {
    	//获取当前登录用户
        UserInfo userInfo = new UserInfo(System.currentTimeMillis()+"",
                intger.getAndIncrement()+"", "./user.png");

        CollaborationMessageList messageList = new CollaborationMessageList(
                userInfo, topicId);
        CollaborationMessageInput messageInput = new CollaborationMessageInput(
                messageList);
        // end::message-list-and-input[]
        CollaborationAvatarGroup avatarGroup = new CollaborationAvatarGroup(
        		userInfo, "tutorial");
        VerticalLayout chatLayout = new VerticalLayout(avatarGroup,messageList,
                messageInput);
        chatLayout.setHeight("500px");
        chatLayout.setWidth("400px");
        chatLayout.expand(messageList);
        messageInput.setWidthFull();
        add(chatLayout);
    }

    /**
     * 用于持久化存储消息
     * @param persister
     * @return
     */
//    CollaborationMessageList createWithPersister(MyMessagePersister persister) {
//        return new CollaborationMessageList(userInfo, "general", persister);
//    }

    private void customSubmitter() {
        TextField field = new TextField("Message");
        Button button = new Button("Submit");
        button.setEnabled(false);

        messageList.setSubmitter(activationContext -> {
            button.setEnabled(true);
            Registration registration = button.addClickListener(
                    event -> activationContext.appendMessage(field.getValue()));
            return () -> {
                registration.remove();
                button.setEnabled(false);
            };
        });
    }

    private void messageConfigurator() {
        UserInfo localUser = this.userInfo;
        CollaborationMessageList collaborationMessageList = new CollaborationMessageList(
                userInfo, topicId);
        collaborationMessageList.setMessageConfigurator((message, user) -> {
            if (user.equals(localUser)) {
                message.addThemeNames("current-user");
            }
        });
        collaborationMessageList.setMessageConfigurator((message, user) -> {
            // Example: Replace all occurrences of "badword" with "***"
            String censored = message.getText().replaceAll("badword", "***");
            message.setText(censored);
        });
    }

}
