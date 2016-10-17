package org.bluemix.challenge;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Button.ClickEvent;

public class Order extends Navigator2 implements View, Button.ClickListener {

	private static final long serialVersionUID = 1L;
	
	private Navigator navigator;

	@Override
	public void enter(ViewChangeEvent event) {

		menuButton1.addClickListener(this);
		menuButton2.setEnabled(false);
		menuButton3.addClickListener(this);			

		navigator = event.getNavigator();
		
		scroll_panel.setContent(new GridView());
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		NativeButton btn = (NativeButton) event.getButton();
		
		if (btn.equals(menuButton1)) {
			navigator.navigateTo("Dashboard");
		} else if (btn.equals(menuButton3)) {
			navigator.navigateTo("About");
		}		
	}
}