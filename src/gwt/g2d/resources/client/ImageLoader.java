/*
 * Copyright 2009 Hao Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.g2d.resources.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.resources.client.ResourceCallback;

/**
 * A helper class for loading an image element asynchronously.
 * 
 * @author hao1300@gmail.com
 */
public final class ImageLoader {
	private ImageLoader() {
	}
	
	/**
	 * Asynchronously loads an {@link ImageElement}.
	 * 
	 * @param src the src of the image.
	 * @param imgResource the image resource to be loaded.
	 * @param callback the callback event when the requested image is loaded.
	 */
	public static void loadImageAsync(String src, 
			AbstractImageElementResource imgResource,
			ResourceCallback<ImageElementResource> callback) {
		final ImageElementExt img = Document.get().createImageElement().cast();
		imgResource.setImage(img);
		img.setupLoadListener(imgResource, callback);
		img.setSrc(src);
	}
	
	/**
	 * Asynchronously loads an {@link ImageElement}.
	 * 
	 * @param src the src of the image.
	 * @param callback the callback event when the requested image is loaded.
	 */
	public static void loadImageAsync(final String src,
			ResourceCallback<ImageElementResource> callback) {
		final ImageElementExt img = Document.get().createImageElement().cast();
		AbstractImageElementResource imgResource = new AbstractImageElementResource() {
			@Override
			public String getName() {
				return "";
			}
			
			@Override
			public int getIndex() {
				return 0;
			}
			
			@Override
			public String getBaseUrl() {
				return src;
			}
		};
		imgResource.setImage(img);
		img.setupLoadListener(imgResource, callback);
		img.setSrc(src);
	}
	
	/**
	 * Helper class that extends the functionality of {@link ImageElement} to
	 * include image load listener without having to attach the image to the 
	 * document.
	 */
	private static final class ImageElementExt extends ImageElement {
		
		protected ImageElementExt() {
		}
		
		/**
		 * Sets up the asynchronous image loading callback.
		 * 
		 * @param imgResource
		 * @param callback
		 */
		public native void setupLoadListener(ImageElementResource imgResource,
				ResourceCallback<ImageElementResource> callback) /*-{
			this.onload = function() {   
        callback.@com.google.gwt.resources.client.ResourceCallback::onSuccess(Lcom/google/gwt/resources/client/ResourcePrototype;)(imgResource);
        this.onload = null;
        this.onerror = null;
			}
			this.onerror = function() {
				var errorMsg = 'IMG tag is bad or the image data is corrupted.';
				var exception = @com.google.gwt.resources.client.ResourceException::new(Lcom/google/gwt/resources/client/ResourcePrototype;Ljava/lang/String;)(imgResource, errorMsg);
        callback.@com.google.gwt.resources.client.ResourceCallback::onError(Lcom/google/gwt/resources/client/ResourceException;)(exception);
        this.onload = null;
        this.onerror = null;
			}
		}-*/;
	}
}
