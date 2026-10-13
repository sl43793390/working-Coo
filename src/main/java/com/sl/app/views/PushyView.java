package com.sl.app.views;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("push")
public class PushyView extends VerticalLayout {
	private FeederThread thread;
	
	Span label;

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		add(new Span("Waiting for updates"));

		label = new Span("更新开始");
		add(label);
		// Start the data feed thread
		thread = new FeederThread(attachEvent.getUI(), this);
		thread.start();
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		// Cleanup
		System.out.println("线程结束。。。");
		thread.interrupt();
		thread = null;
	}

	private static class FeederThread extends Thread {
		private final UI ui;
		private final PushyView view;

		private int count = 0;

		public FeederThread(UI ui, PushyView view) {
			this.ui = ui;
			this.view = view;
		}

		@Override
		public void run() {
			try {
				// Update the data for a while
				while (count < 10) {
					// Sleep to emulate background work
					Thread.sleep(1000);
					String message = "This is update " + count++;

					ui.access(() -> view.label.setText(message));
//					ui.access(() -> view.add(new Span(message)));
				}

				// Inform that we're done
				ui.access(() -> {
					view.add(new Span("Done updating"));
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}