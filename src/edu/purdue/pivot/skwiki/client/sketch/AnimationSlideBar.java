package edu.purdue.pivot.skwiki.client.sketch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.kiouri.sliderbar.client.view.SliderBarHorizontal;

public class AnimationSlideBar extends SliderBarHorizontal {

	interface ImagesSliderBarSimpleVertical extends ClientBundle {

		@Source("kdehdrag.png")
		ImageResource drag();

		@Source("kdehless.png")
		ImageResource less();

		@Source("kdehmore.png")
		ImageResource more();

		@Source("kdehscale.png")
		DataResource scalev();
	}

	ImagesSliderBarSimpleVertical images = GWT
			.create(ImagesSliderBarSimpleVertical.class);

	public AnimationSlideBar(int maxValue, String width, boolean showRows) {
		if (showRows) {
			setLessWidget(new Image(images.less()));
			setScaleWidget(new Image(images.scalev().getUrl()), 3);
			setMoreWidget(new Image(images.more()));
		} else {
			setScaleWidget(new Image(images.scalev().getUrl()), 10);
		}
		setDragWidget(new Image(images.drag()));
		this.setWidth(width);
		this.setMaxValue(maxValue);
	}

}