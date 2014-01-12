package edu.purdue.pivot.skwiki.client.text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class MessageDialogBox extends DialogBox {

  private HTML html;
  private Button button;

  public MessageDialogBox() {
    super(true);

    html = new HTML();
    button = new Button("OK");

    VerticalPanel basePanel = new VerticalPanel();
    basePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    basePanel.add(html);
    basePanel.add(button);

    button.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        hide();
      }
    });

    setWidget(basePanel);
  }

  public void show(final String title, final String message) {
    setText(title);
    html.setHTML(message);
    center();
    show();
  }
}