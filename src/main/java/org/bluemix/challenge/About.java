package org.bluemix.challenge;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Button.ClickEvent;

public class About extends Navigator3 implements View, Button.ClickListener {
	private static final long serialVersionUID = 1L;
	
	private Navigator navigator;

	@Override
	public void enter(ViewChangeEvent event) {

		menuButton1.addClickListener(this);
		menuButton2.addClickListener(this);	
		menuButton3.setEnabled(false);		

		navigator = event.getNavigator();
		
		htmlContent.setCaptionAsHtml(true);
		String Message = "<h1>Vaadin Challenge</h1><br>";
		Message = Message + "<h3>This application represents task five of<br>";
		Message = Message + "the Vaadin Challenge designed to familiarise<br>";
		Message = Message + "developers with the power of the Vaadin Java<br>";
		Message = Message + "Framework on the IBM Bluemix Cloud Platform.</h3>";
		htmlContent.setCaption(Message);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		NativeButton btn = (NativeButton) event.getButton();
		
		if (btn.equals(menuButton1)) {
			navigator.navigateTo("Dashboard");
		} else if (btn.equals(menuButton2)) {
			navigator.navigateTo("Order");
		}		
	}
}