package edu.purdue.pivot.skwiki.client;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;


public class LoginPage extends Composite {
	private TextBox textBoxUsername;
	private PasswordTextBox textBoxPassword;
	private TextBox textBoxID;

	public LoginPage() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		Label lblLoginToYour = new Label("Sign in to your account");
		lblLoginToYour.setStyleName("gwt-Label-Login");
		verticalPanel.add(lblLoginToYour);
		
		FlexTable flexTable = new FlexTable();
		verticalPanel.add(flexTable);
		flexTable.setWidth("345px");
		
		Label lblUsername = new Label("Username:");
		lblUsername.setStyleName("gwt-Label-Login");
		flexTable.setWidget(0, 0, lblUsername);
		
		textBoxUsername = new TextBox();
		flexTable.setWidget(0, 1, textBoxUsername);
		
		Label lblProject = new Label("Project ID:");
		lblProject.setStyleName("gwt-Label-Login");
		flexTable.setWidget(1, 0, lblProject);
		
		textBoxID = new TextBox();
		flexTable.setWidget(1, 1, textBoxID);
		
		
		Label lblPassword = new Label("Password:");
		lblPassword.setStyleName("gwt-Label-Login");
		flexTable.setWidget(2, 0, lblPassword);
		
		textBoxPassword = new PasswordTextBox();
		flexTable.setWidget(2, 1, textBoxPassword);
		
		CheckBox chckbxRememberMeOn = new CheckBox("Remember me");
		chckbxRememberMeOn.setStyleName("gwt-Login-CheckBox");
		flexTable.setWidget(3, 0, chckbxRememberMeOn);
		
		
	}
	
	public String getUserName() {
		return textBoxUsername.getText();	
	}
	
	public String getPassword() {
		return textBoxPassword.getText();
	}

	public String getID() {
		return textBoxID.getText();
	}
}
