package edu.purdue.pivot.skwiki.client.text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.BasicFormatter;

public final class ColorPicker extends DecoratedPopupPanel {

  private static final int ROWS = 13;
  private static final int COLUMNS = 15;
  private static final String[][] COLOURS = {
    {
      "#190707", "#2a0a0a", "#3b0b0b", "#610b0b", "#8a0808", "#b40404", "#df0101", "#ff0000",
      "#fe2e2e", "#fa5858", "#f78181", "#f5a9a9", "#f6cece", "#f8e0e0", "#fbefef"
    },
    {
      "#191007", "#2a1b0a", "#3b240b", "#61380b", "#8a4b08", "#b45f04", "#df7401", "#ff8000",
      "#fe9a2e", "#faac58", "#f7be81", "#f5d0a9", "#f6e3ce", "#f8ece0", "#fbf5ef"
    },
    {
      "#181907", "#292A0A", "#393B0B", "#5e610b", "#868A08", "#AEB404", "#D7DF01", "#FFFF00",
      "#F7FE2E", "#F4FA58", "#F3F781", "#F2F5A9", "#F5F6CE", "#F7F8E0", "#FBFBEF"
    },
    {
      "#101907", "#1B2A0A", "#243b0b", "#38610B", "#4B8A08", "#5FB404", "#74DF00", "#80FF00",
      "#9AFE2E", "#ACFA58", "#BEF781", "#D0F5A9", "#E3F6CE", "#ECF8E0", "#F5FBEF"
    },
    {
      "#071907", "#0A2A0A", "#0B3B0B", "#0B610B", "#088A08", "#04B404", "#01DF01", "#00FF00",
      "#2EFE2E", "#58FA58", "#81F781", "#A9F5A9", "#CEF6CE", "#E0F8E0", "#EFFBEF"
    },
    {
      "#071910", "#0A2A1B", "#0B3B24", "#0B6138", "#088A4B", "#04B45F", "#01DF74", "#00FF80",
      "#2EFE9A", "#58FAAC", "#81F7BE", "#A9F5D0", "#CEF6E3", "#E0F8EC", "#EFFBF5"
    },
    {
      "#071918", "#0A2A29", "#0B3B39", "#0B615E", "#088A85", "#04B4AE", "#01DFD7", "#00FFFF",
      "#2EFEF7", "#58FAF4", "#81F7F3", "#A9F5F2", "#CEF6F5", "#E0F8F7", "#EFFBFB"
    },
    {
      "#071019", "#0A1B2A", "#0B243B", "#0B3861", "#084B8A", "#045FB4", "#0174DF", "#0080FF",
      "#2E9AFE", "#58ACFA", "#81BEF7", "#A9D0F5", "#CEE3F6", "#E0ECF8", "#EFF5FB"
    },
    {
      "#070719", "#0A0A2A", "#0B0B3B", "#0B0B61", "#08088A", "#0404B4", "#0101DF", "#0000FF",
      "#2E2EFE", "#5858FA", "#8181F7", "#A9A9F5", "#CECEF6", "#E0E0F8", "#EFEFFB"
    },
    {
      "#100719", "#1B0A2A", "#240B3B", "#380B61", "#4B088A", "#5F04B4", "#7401DF", "#8000FF",
      "#9A2EFE", "#AC58FA", "#BE81F7", "#D0A9F5", "#E3CEF6", "#ECE0F8", "#F5EFFB"
    },
    {
      "#190718", "#2A0A29", "#3B0B39", "#610B5E", "#8A0886", "#B404AE", "#DF01D7", "#FF00FF",
      "#FE2EF7", "#FA58F4", "#F781F3", "#F5A9F2", "#F6CEF5", "#F8E0F7", "#FBEFFB"
    },
    {
      "#190710", "#2A0A1B", "#3B0B24", "#610B38", "#8A084B", "#B4045F", "#DF0174", "#FF0080",
      "#FE2E9A", "#FA58AC", "#F781BE", "#F5A9D0", "#F6CEE3", "#F8E0EC", "#FBEFF5"
    },
    {
      "#000000", "#0b0b0b", "#151515", "#1c1c1c", "#2e2e2e", "#424242", "#585858", "#6e6e6e",
      "#848484", "#a4a4a4", "#bdbdbd", "#d8d8d8", "#e6e6e6", "#f2f2f2", "#FFFFFF"
    }
  };
  private Grid grid = new Grid(ROWS, COLUMNS);
  private BasicFormatter basicFormatter;

  public ColorPicker(final RichTextArea richTextArea) {
    super(true);

    this.basicFormatter = richTextArea.getBasicFormatter();

    grid.setCellPadding(0);
    grid.setCellSpacing(0);

    for (int row = 0; row < ROWS; row++) {
      for (int column = 0; column < COLUMNS; column++) {
        final String colour = COLOURS[row][column];
        PushButton colourButton = new PushButton();
        Element colourButtonElement = colourButton.getElement();

        DOM.setStyleAttribute(colourButtonElement, "background", colour);
        DOM.setStyleAttribute(colourButtonElement, "width", "8px");
        DOM.setStyleAttribute(colourButtonElement, "height", "8px");

        colourButton.addClickHandler(new ClickHandler() {

          @Override
          public void onClick(final ClickEvent event) {
            if (null != basicFormatter) {
              basicFormatter.setForeColor(colour);
            }
            hide();
          }
        });

        grid.setWidget(row, column, colourButton);
      }
    }

    this.add(grid);
  }
}
