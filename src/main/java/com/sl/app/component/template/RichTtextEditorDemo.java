package com.sl.app.component.template;

import com.sl.app.component.util.ComFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;

public class RichTtextEditorDemo extends VerticalLayout{

	public RichTtextEditorDemo() {
		super();
		String str = "<h2>High quality rich text editor for the web</h2>\r\n"
				+ "<p>Rich text editor handles the following formatting:</p>\r\n"
				+ "<ul>\r\n"
				+ " <li>Bold</li>\r\n"
				+ " <li><em>Italic</em></li>\r\n"
				+ " <li><u>Underline</u></li>\r\n"
				+ " <li><s>Strike-through</s></li>\r\n"
				+ " <li>Headings (H1, H2, H3)</li>\r\n"
				+ " <li>Lists (ordered and unordered)</li>\r\n"
				+ " <li>Text align (left, center, right)</li>\r\n"
				+ " <li><sub>Sub</sub>script and <sup>super</sup>script</li>\r\n"
				+ " <li><a href=\"https://vaadin.com/\" rel=\"nofollow\">Hyperlink</a></li>\r\n"
				+ "</ul>\r\n"
				+ "<p>In addition to text formatting, additional content blocks can be added.</p>\r\n"
				+ "";
		RichTextEditor edit = new RichTextEditor();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		edit.setHeight("200px");//设置高度并不会自动出现滚动条
		edit.setWidth("80%");
		
		setHeight("700px");
		edit.asHtml().setValue(str);
		add(edit);
		
		Button primaryBtn = ComFactory.getPrimaryBtn("打印");
		primaryBtn.setIcon(new Icon(VaadinIcon.PRINT));
		primaryBtn.addClickListener(e -> System.out.println(edit.getValue()));
		add(primaryBtn);
	}

	
	

}
