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

/**
 * Represents an abstract implementation of {@link ImageElementResource}.
 * 
 * @author hao1300@gmail.com
 */
public abstract class AbstractImageElementResource 
		implements ImageElementResource {
	private ImageElement img;
	
	@Override
	public ImageElement getImage() {
		return img;
	}
	
	/**
	 * Sets the {@link ImageElement} in this resource.
	 */
	public void setImage(ImageElement img) {
		this.img = img;
	}
}
