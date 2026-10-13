package com.sl.app.component.template;

import java.util.ResourceBundle;

import com.sl.app.component.util.ComFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.Upload;

/**
 * 上传示例：已迁移到 Vaadin 24.8+ 新 API（UploadHandler 直接落盘）。
 * 落盘、保存成功通知都由 ComFactory.getUpload(dir, onSaved) 内部处理，
 * 不再使用废弃的 Receiver / MultiFileBuffer / FileReceiverImpl。
 *
 * @author Administrator
 *
 */
public class UploadDemo extends VerticalLayout{

	private static final long serialVersionUID = -7599457982054001810L;

	public UploadDemo() {
		super();
		String customDir = ResourceBundle.getBundle("application").getString("upload.custom.dir");
		Upload upload = ComFactory.getUpload(customDir, null);
		upload.setMaxFiles(1);
		//限制文件类型
		upload.setAcceptedFileTypes("application/pdf", ".pdf",".jpg",".png","docx","xls","xlsx","zip");
		//添加监听器
		upload.addFileRejectedListener(event -> {
			String errorMessage = event.getErrorMessage();
			Notification notification = Notification.show(errorMessage, 5000,
					Notification.Position.MIDDLE);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		});

		add(upload);
	        //进度条
	        ProgressBar progressBar = new ProgressBar();
	        progressBar.setIndeterminate(true);

	        Div progressBarLabel = new Div();
	        progressBarLabel.setText("上传进行中，请稍等...");

	        add(progressBarLabel, progressBar);
	        
//	        完成后进行关闭或隐藏进度条
	        Button primaryBtn = ComFactory.getPrimaryBtn("完成");
//	        primaryBtn.addClickListener(e -> {progressBar.setVisible(false);progressBarLabel.setVisible(false)});
	        primaryBtn.addClickListener(e -> this.remove(progressBar,progressBarLabel));//完成后直接移除进度条组件
	        add(primaryBtn);
	        
	        /**************************文件下载**************/
			Button downloadBtn = ComFactory.getPrimaryBtn("下载");
			primaryBtn.addClickListener(event -> {
//				文件下载只需要访问对应接口，发送制定文件名即可${pageContext.request.contextPath}/file/download?fileName=aa.txt
				UI.getCurrent().getPage().open("file/download2?fileName=abc.txt", "123");
			});
		        add(downloadBtn);
		        
		     DatePickerIndividualInputFields f = new DatePickerIndividualInputFields();
		     add(f);
	        
	}

}
