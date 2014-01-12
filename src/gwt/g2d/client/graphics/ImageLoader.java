/*
 * Copyright 2008 Google Inc.
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
package gwt.g2d.client.graphics;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.resources.client.ClientBundle;

import gwt.g2d.resources.client.ExternalImageResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Provides a mechanism for deferred execution of a callback 
 * method once all specified Images are loaded.
 * <p>
 * This class may be deprecated in future release. Considers using 
 * {@link ClientBundle} with {@link ExternalImageResource} instead.
 * <p>
 * If dynamic image loading is required, please consider using
 * {@link gwt.g2d.resources.client.ImageLoader} instead of this one.
 * <p>
 * (This is a slightly modified by hao1300@gmail.com from the GWT-incubator's 
 * ImageLoader to provide a more flexible loading scheme.)
 */
public class ImageLoader {

  /**
   * Interface to allow anonymous instantiation of a CallBack
   * Object with method that gets invoked when all the images
   * are loaded.
   */
  public interface CallBack {
    void onImagesLoaded(ImageElement[] imageElements);
  }
  
  /**
   * Static internal collection of ImageLoader instances.
   * ImageLoader is not instantiable externally.
   */
  private static Set<ImageLoader> imageLoaders = new HashSet<ImageLoader>();
  
  /**
   * Takes in an url String corresponding to the image needed to be loaded. 
   * The onImagesLoaded() method in the specified CallBack
   * object is invoked with an array of ImageElements corresponding to
   * the where the first element of the array is the image requested.
   * 
   * @param url Array of urls for the images that need to be loaded
   * @param cb CallBack object
   */
  public static void loadImages(String url, CallBack cb) {
  	ImageLoader il = new ImageLoader();
    il.finalize(cb);
    ImageLoader.imageLoaders.add(il);
    il.totalImages = 1;
  	ImageElement imageElement = il.prepareImage();
  	il.images.add(imageElement);
  	imageElement.setSrc(url);
  }
  
  /**
   * Takes in an array of url Strings corresponding to the images needed to
   * be loaded. The onImagesLoaded() method in the specified CallBack
   * object is invoked with an array of ImageElements corresponding to
   * the original input array of url Strings once all the images report
   * an onload event.
   * 
   * @param urls Array of urls for the images that need to be loaded
   * @param cb CallBack object
   */
  public static void loadImages(String[] urls, CallBack cb) {
    loadImages(Arrays.asList(urls), cb);
  }
  
  /**
   * Takes in a list of url Strings corresponding to the images needed to
   * be loaded. The onImagesLoaded() method in the specified CallBack
   * object is invoked with a list of ImageElements corresponding to
   * the original input list of url Strings once all the images report
   * an onload event.
   * 
   * @param urls List of urls for the images that need to be loaded
   * @param cb CallBack object
   */
  public static void loadImages(List<String> urls, CallBack cb) {
    ImageLoader il = new ImageLoader();
    il.finalize(cb);
    ImageLoader.imageLoaders.add(il);
    il.totalImages = urls.size();
    for (String url : urls) {
    	ImageElement imageElement = il.prepareImage();
    	il.images.add(imageElement);
    	imageElement.setSrc(url);
    }
  }
  
  private CallBack callBack = null;
  private List<ImageElement> images = new ArrayList<ImageElement>();
  private int loadedImages = 0;
  private int totalImages = 0;
  
  private ImageLoader() {
  }
  
  /**
   * Invokes the onImagesLoaded method in the CallBack if all the
   * images are loaded AND we have a CallBack specified.
   * 
   * Called from the JSNI onload event handler.
   */
  private void dispatchIfComplete() {
    if (callBack != null && isAllLoaded()) {
      callBack.onImagesLoaded(images.toArray(new ImageElement[0]));
      // remove the image loader
      ImageLoader.imageLoaders.remove(this);
    }
  }
  
  /**
   * Sets the callback object for the ImageLoader.
   * Once this is set, we may invoke the callback once all images that
   * need to be loaded report in from their onload event handlers.
   * 
   * @param cb
   */
  private void finalize(CallBack cb) {
    this.callBack = cb;
  }
  
  private void incrementLoadedImages() {
    this.loadedImages++;
  }
  
  private boolean isAllLoaded() {
    return (loadedImages == totalImages);
  }
  
  /**
   * Returns a handle to an img object. Ties back to the ImageLoader instance
   */
  private native ImageElement prepareImage() /*-{
    // if( callback specified )
    // do nothing
     
    var img = new Image();
    var __this = this;
     
    img.onload = function() {
      if(!img.__isLoaded) {
       
        // __isLoaded should be set for the first time here.
        // if for some reason img fires a second onload event
        // we do not want to execute the following again (hence the guard)
        img.__isLoaded = true;       
        __this.@gwt.g2d.client.graphics.ImageLoader::incrementLoadedImages()();
        img.onload = null;
        
        // we call this function each time onload fires
        // It will see if we are ready to invoke the callback
        __this.@gwt.g2d.client.graphics.ImageLoader::dispatchIfComplete()();   
      } else {
        // we invoke the callback since we are already loaded
        __this.@gwt.g2d.client.graphics.ImageLoader::dispatchIfComplete()();   
      }
    }
    
    return img;
  }-*/;
}
