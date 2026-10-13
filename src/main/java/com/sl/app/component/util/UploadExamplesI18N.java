package com.sl.app.component.util;

import com.vaadin.flow.component.upload.UploadI18N;

import java.util.Arrays;

/**
 * Provides a default I18N configuration for the Upload examples
 *
 * At the moment the Upload component requires a fully configured I18N instance,
 * even for use-cases where you only want to change individual texts.
 *
 * This I18N configuration is an adaption of the web components I18N defaults
 * and can be used as a basis for customizing individual texts.
 */
public class UploadExamplesI18N extends UploadI18N {
	private static final long serialVersionUID = 5198531494081865692L;

	public UploadExamplesI18N() {
        setDropFiles(new DropFiles().setOne("拖动文件到这里")
                .setMany("拖动文件到这里"));
        setAddFiles(new AddFiles().setOne("选择文件")
                .setMany("选择文件"));
        setError(new Error().setTooManyFiles("Too Many Files.")
                .setFileIsTooBig("File is Too Big.")
                .setIncorrectFileType("Incorrect File Type."));
        setUploading(new Uploading()
                .setStatus(new Uploading.Status().setConnecting("Connecting...")
                        .setStalled("Stalled")
                        .setProcessing("Processing File...").setHeld("Queued"))
                .setRemainingTime(new Uploading.RemainingTime()
                        .setPrefix("remaining time: ")
                        .setUnknown("unknown remaining time"))
                .setError(new Uploading.Error()
                        .setServerUnavailable(
                                "Upload failed, please try again later")
                        .setUnexpectedServerError(
                                "Upload failed due to server error")
                        .setForbidden("Upload forbidden")));
        setUnits(new Units().setSize(Arrays.asList("B", "kB", "MB", "GB", "TB",
                "PB", "EB", "ZB", "YB")));
    }
}