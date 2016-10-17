package org.bluemix.challenge;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("mytheme")
@Widgetset("org.bluemix.challenge.MyAppWidgetset")
public class MyUI extends UI {
	
	private static final long serialVersionUID = 1L;
	
	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {
		
		navigator = new Navigator(this, this);
		
		navigator.addView("Login", new Login());
		navigator.addView("Dashboard", new Dashboard());
		navigator.addView("Order", new Order());
		navigator.addView("About",  new About());
			
		navigator.navigateTo("Login");
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

		private static final long serialVersionUID = 1L;
    }
}
