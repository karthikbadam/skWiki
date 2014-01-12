package edu.purdue.pivot.skwiki.client.text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.ExtendedFormatter;
import com.google.gwt.user.client.ui.TextBox;

public final class LinkPopup extends DecoratedPopupPanel {

  private TextBox textBox;
  private Button button;
  private ExtendedFormatter extendedFormatter;

  public LinkPopup(final RichTextArea richTextArea) {
    super(true);
    this.extendedFormatter = richTextArea.getExtendedFormatter();

    HorizontalPanel basePanel = new HorizontalPanel();

    textBox = new TextBox();
    button = new Button("OK", new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        extendedFormatter.createLink(textBox.getText());
        hide();
      }
    });

    basePanel.add(textBox);
    basePanel.add(button);

    this.add(basePanel);
  }

  @Override
  public void show() {
    textBox.setText("http://");
    super.show();
  }
}
