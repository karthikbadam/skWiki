package edu.purdue.pivot.skwiki.client.dnd;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

class Tooltip extends PopupPanel {
    private int delay;

    public Tooltip(Widget sender, int offsetX, int offsetY, 
        final String text, final int delay, final String styleName) {
      super(true);

      this.delay = delay;

      HTML contents = new HTML(text);
      add(contents);

      int left = sender.getAbsoluteLeft() + offsetX;
      int top = sender.getAbsoluteTop() + offsetY;

      setPopupPosition(left, top);
      setStyleName(styleName);
    }

    @Override
	public void show() {
      super.show();

      Timer t = new Timer() {

        @Override
		public void run() {
          Tooltip.this.hide();
        }

      };
      t.schedule(delay);
    }
  }
