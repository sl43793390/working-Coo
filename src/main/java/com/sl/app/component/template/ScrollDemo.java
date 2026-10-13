package com.sl.app.component.template;

import java.time.LocalDate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
/**
 * scroll 使用注意事项：
 * 1、 extends VerticalLayout继承一个layout
 * 2、设置当前这个总layout的高度：setWidth("100%");
		setHeight("770px");该layout不放内容，只放置滚动组件
	3.再创建一个layout，用于放入到滚动窗口中，该layout放置所有内容：
	 mainLayout = ComFactory.getVerticalLayout();
        mainLayout.add(ComFactory.getTitle("我的代办清单"));
		mainLayout.add(ComFactory.getHorizontalLine());
	4、创建滚动组件并放置第三步的layout
		Scroller sc = new Scroller(mainLayout);//参数就是第三步的layout
		sc.setScrollDirection(ScrollDirection.VERTICAL);
		add(sc);//放入大layout中
		
 * @author Administrator
 *
 */
@Service
@Scope("prototype")
public class ScrollDemo extends VerticalLayout {

	private static final long serialVersionUID = 4512289455462777038L;

	public static String PERSONAL_TITLE_ID = "ssss";
	public ScrollDemo() {
		  setAlignItems(Alignment.STRETCH);
	        setMaxWidth("30%");
	        setPadding(false);
	        setSpacing(false);
	        getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");

	        // Header
	        Header header = new Header();
	        header.getStyle().set("align-items", "center")
	                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
	                .set("display", "flex").set("padding", "var(--lumo-space-m)");

	        H2 editEmployee = new H2("Edit employee");
	        editEmployee.getStyle().set("margin", "0");

	        Icon arrowLeft = VaadinIcon.ARROW_LEFT.create();
	        arrowLeft.setSize("var(--lumo-icon-size-m)");
	        arrowLeft.getElement().setAttribute("aria-hidden", "true");
	        arrowLeft.getStyle().set("box-sizing", "border-box")
	                .set("margin-right", "var(--lumo-space-m)")
	                .set("padding", "calc(var(--lumo-space-xs) / 2)");

	        Anchor goBack = new Anchor("#", arrowLeft);

	        header.add(goBack, editEmployee);
	        add(header);
	        
		// Personal information
		H3 personalTitle = new H3("Personal information");
		personalTitle.setId(PERSONAL_TITLE_ID);

		TextField firstName = new TextField("First name");
		firstName.setWidthFull();

		TextField lastName = new TextField("Last name");
		lastName.setWidthFull();

		DatePicker birthDate = new DatePicker("Birthdate");
		birthDate.setInitialPosition(LocalDate.of(1990, 1, 1));
		birthDate.setWidthFull();

		Section personalInformation = new Section(personalTitle, firstName,
		        lastName, birthDate);
		personalInformation.getElement().setAttribute("aria-labelledby",
		        PERSONAL_TITLE_ID);

		// Employment information
		H3 employmentTitle = new H3("Employment information");
		employmentTitle.setId("sdassaa");

		TextField position = new TextField("Position");
		position.setWidthFull();

		TextArea additionalInformation = new TextArea("Additional Information");
		additionalInformation.setWidthFull();

		Section employmentInformation = new Section(employmentTitle, position,
		        additionalInformation);
		employmentInformation.getElement().setAttribute("aria-labelledby",
		        "ppooopp");

		// NOTE
		// We are using inline styles here to keep the sl simple.
		// We recommend placing CSS in a separate style sheet and to
		// encapsulating the styling in a new component.
		Scroller scroller = new Scroller(
		        new Div(personalInformation, employmentInformation));
		scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
		scroller.getStyle()
		        .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
		        .set("padding", "var(--lumo-space-m)");
		scroller.setHeight("300px");
		add(scroller);
		
		 // Footer
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.getStyle().set("margin-right", "var(--lumo-space-s)");

        Button reset = new Button("Reset");
        reset.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Footer footer = new Footer(save, reset);
        footer.getStyle().set("padding", "var(--lumo-space-wide-m)");
        add(footer);
	}

	
}
