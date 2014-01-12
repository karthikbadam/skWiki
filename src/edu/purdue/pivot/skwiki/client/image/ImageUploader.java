package edu.purdue.pivot.skwiki.client.image;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImageUploader extends DialogBox {
	VerticalPanel p;
	PreloadedImage uploadedImage = null;

	public ImageUploader(int width, int height) {

		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		this.add(defaultUploader);
	}

	public PreloadedImage getImage() {
		return uploadedImage;
	}

	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		@Override
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {

				new PreloadedImage(uploader.fileUrl(), showImage);
				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				System.out.println("Server message " + info.message);
			}
		}
	};

	// Attach an image to the pictures viewer
	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
		@Override
		public void onLoad(PreloadedImage image) {
			image.setWidth("75px");
			uploadedImage = image;
		}
	};

}
