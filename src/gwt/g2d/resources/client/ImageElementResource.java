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

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.resources.client.ResourcePrototype;

/**
 * Represents a resource that stores an ImageElement.
 * This class should only be used with {@link ExternalImageResource}.
 * 
 * @author hao1300@gmail.com
 */
public interface ImageElementResource extends ResourcePrototype {
	
	/**
	 * Gets the ImageElement in this resource.
	 */
	ImageElement getImage();
	
	/**
	 * Gets the original image's base url, that is, the original filename of the 
	 * image. This not the deployed url, which may vary for each compilation.
	 */
	String getBaseUrl();
	
	/**
	 * Gets the zero-based index of this image in the requested image list.
	 * Note that an image with a smaller index may not necessarily load first. 
	 */
	int getIndex();
}
