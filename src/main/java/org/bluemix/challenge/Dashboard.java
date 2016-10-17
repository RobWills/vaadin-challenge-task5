package org.bluemix.challenge;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.vaadin.addon.charts.Chart;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;

public class Dashboard extends Navigator1 implements View, Button.ClickListener, Field.ValueChangeListener {

	private static final long serialVersionUID = 1L;
	
	private Navigator navigator;
	private Date endDate;
	private Date startDate;
	private String Symbol = "";

	@Override
	public void enter(ViewChangeEvent event) {
		
		// Dashboard creation code
		
		if (event.getOldView() != null) {

			navigator = event.getNavigator();
			
			menuButton1.setEnabled(false);
			menuButton2.addClickListener(this);
			menuButton3.addClickListener(this);
			createChartBtn.addClickListener(this);

			// Get a calendar which is set to a specified date.
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(GregorianCalendar.DAY_OF_MONTH, -1);

			// Set value of field to yesterday and make read-only
			endDate = calendar.getTime();
			endDateFld.setReadOnly(false);
			endDateFld.setValue(endDate);
			endDateFld.setReadOnly(true);
			endDateFld.addValueChangeListener(this);

			// Set default value of field to one year ago

			calendar.add(GregorianCalendar.YEAR, -1);

			// Set default value of field to yesterday
			startDate = calendar.getTime();
			startDateFld.setValue(startDate);
			startDateFld.addValueChangeListener(this);
			
			calendar = null;
			
			companyComboBox.addValueChangeListener(this);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		Button btn = event.getButton();
		
		if (btn.equals(createChartBtn)) {
			if (Symbol.equals("")) {
				Notification.show("Select ticker symbol for company first", Notification.Type.ERROR_MESSAGE);
				companyComboBox.focus();
			} else {
				
				SharePriceChart shares = new SharePriceChart();
				if (shares.getShareData(Symbol, startDate, endDate)) {
					
					Iterator<Component> iterate = dashboardLayout.iterator();
			        while (iterate.hasNext()) {
			            Component c = iterate.next();
			            if (c.getClass().toString().equals("class com.vaadin.addon.charts.Chart")) {
			            	sharePriceChart = (Chart) c;
			            	break;
			            }
			        }
					
			        Chart newChart = shares.getChart();
			        if (newChart != null) {
			        	dashboardLayout.replaceComponent(sharePriceChart, newChart);

			        	sharePriceMessage.setCaptionAsHtml(true);
			        	sharePriceMessage.setCaption("<br/>Data supplied by Markit On Demand. Commercial use <a href=\"http://dev.markitondemand.com/MODApis/\">prohibited</a>.");
			        	sharePriceMessage.setVisible(true);
			        } else {
			        	Notification.show("Cannot create chart", Notification.Type.ERROR_MESSAGE);
			        }
				} else {
					Notification.show("Invalid data", Notification.Type.ERROR_MESSAGE);
				}			
			}
		} else {
			NativeButton nativeBtn = (NativeButton) event.getButton();
			if (nativeBtn.equals(menuButton2)) {
				navigator.navigateTo("Order");
			} else if (nativeBtn.equals(menuButton3)) {
				navigator.navigateTo("About");
			}
		}		
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		Symbol = (String) companyComboBox.getValue();
		startDate = startDateFld.getValue();
		endDate = endDateFld.getValue();
	}
}