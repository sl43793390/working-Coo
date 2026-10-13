package com.sl.app.component.template;

import com.flowingcode.vaadin.addons.xterm.ITerminalClipboard;
import com.flowingcode.vaadin.addons.xterm.ITerminalOptions;
import com.flowingcode.vaadin.addons.xterm.XTerm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class XtermDemo extends VerticalLayout{

    private static final long serialVersionUID = 1979266644545963431L;

    public XtermDemo() {
        System.out.println("初始化开始========");
        XTerm xterm = new XTerm();
        xterm.writeln("Hello world.\n\n");
        xterm.setCursorBlink(true);
        xterm.setCursorStyle(ITerminalOptions.CursorStyle.UNDERLINE);

        xterm.setSizeFull();
        xterm.setCopySelection(true);
        xterm.setUseSystemClipboard(ITerminalClipboard.UseSystemClipboard.READWRITE);
        xterm.setPasteWithRightClick(true);

        xterm.addLineListener(ev->{
            String line = ev.getLine();
            System.out.println(line);
        });

        xterm.focus();

        xterm.fit();

        add(xterm);

    }


}
