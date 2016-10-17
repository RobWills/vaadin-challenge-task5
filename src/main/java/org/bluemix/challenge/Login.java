package org.bluemix.challenge;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;

public class Login extends LoginDesign implements View, Button.ClickListener {

	private static final long serialVersionUID = 1L;
	
	private Navigator navigator;
	
	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getOldView() != null) {
			navigator = event.getNavigator();
			usernameFld.focus();
			loginBtn.addClickListener(this);
			loginBtn.setClickShortcut(KeyCode.ENTER);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		Button btn = event.getButton();
		if (btn.equals(loginBtn)) {

			// Check login credentials and, if authentic, load the grid view

			if (usernameFld.getValue().equals("vaadin") && passwordFld.getValue().equals("bluemix")) {
				navigator.removeView("Login");
				navigator.navigateTo("Dashboard");
			} else {
				Notification.show("Invalid username or password");
			}
		}
	}
}
