package com.sl.app.views;

import java.nio.file.Paths;

import com.github.olafj.vaadin.flow.Video;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "video")
//@AnonymousAllowed
public class VideoView extends VerticalLayout {

    private static final long serialVersionUID = 8728059005569047780L;
	
    public VideoView() {
    	
    	Video video = new Video();
    	video.setWidth("540px");
    	video.setControls(true);
    	video.setSource(Paths.get("d:\\test.mkv"));
//    	video.setPosterSource(Paths.get("./target/test-classes/cover.jpeg"));
		add(video);
    }


}
