package edu.purdue.pivot.skwiki.client.sketch.colorpicker;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ColorPickerSample implements EntryPoint, ClickListener
{
	private ColorPickerDialog pickerDialog;

	private static class ColorPickerDialog extends DialogBox
	{
		private ColorPicker picker;

		public ColorPickerDialog()
		{
			setText("Choose a color");

			// Define the panels
			VerticalPanel panel = new VerticalPanel();
			FlowPanel okcancel = new FlowPanel();
			picker = new ColorPicker();

			// Define the buttons
			Button ok = new Button("Ok");	// ok button
			ok.addClickListener(new ClickListener()
			{
				@Override
				public void onClick(Widget sender)
				{
					Window.alert("You chose " + picker.getHexColor());
					ColorPickerDialog.this.hide();
				}
			});

			Button cancel = new Button("Cancel");	// cancel button
			cancel.addClickListener(new ClickListener()
			{
				@Override
				public void onClick(Widget sender)
				{
					ColorPickerDialog.this.hide();
				}
			});
			okcancel.add(ok);
			okcancel.add(cancel);

			// Put it together
			panel.add(picker);
			panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			panel.add(okcancel);

			setWidget(panel);
		}
	}

	@Override
	public void onModuleLoad()
	{
		pickerDialog = new ColorPickerDialog();

		Button b = new Button("Click me");
		b.addClickListener(this);

		RootPanel.get().add(b);
	}

	@Override
	public void onClick(Widget sender)
	{
		// Show the dialog box.
		pickerDialog.show();
	}
}
