package edu.purdue.pivot.skwiki.client.dnd;

import com.google.gwt.user.client.ui.HTML;

/**
 * Demonstrate a draggable widget.
 */
public final class RedBoxDraggableWidget extends HTML {

  private static int counter;

  private static final String CSS_DEMO_RED_BOX_DRAGGABLE_WIDGET = "demo-red-box-draggable-widget";

  private static final int DRAGGABLE_SIZE = 65;

  public RedBoxDraggableWidget() {
    setPixelSize(DRAGGABLE_SIZE, DRAGGABLE_SIZE);
    setHTML("<i>drag me!</i> draggable widget #" + ++counter);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    addStyleName(CSS_DEMO_RED_BOX_DRAGGABLE_WIDGET);
  }
}
