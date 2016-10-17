package org.bluemix.challenge;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ContainerDataSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.data.util.BeanItemContainer;

public class SharePriceChart {

	static final String OpenCurlyBracket = "%7B";
	static final String CloseCurlyBracket = "%7D";
	static final String OpenSquareBracket = "%5B";
	static final String CloseSquareBracket = "%5D";
	static final String Colon = "%3A";
	static final String Comma = "%2C";
	static final String Quote = "%22";
	static final long millisecsPerDay = 86400000;
	
	private String Symbol;
	private int NumberDays;
	private String Currency;
	private JSONArray Values;
	private JSONArray Dates;
	private boolean DataValidated = false;
	
	protected SharePriceChart() {
	}

	protected boolean getShareData(String SymbolReqd, Date startDate, Date endDate) {

		DataValidated = false;
		Symbol = SymbolReqd;
		
		// Calculate the number of days between the two dates
		
		long milliseconds = endDate.getTime() - startDate.getTime();
		NumberDays = (int) (milliseconds / millisecsPerDay);
		// System.out.println("Number of days is " + NumberDays);
		
		if (NumberDays < 1) {
			return false;
		}

		String Resource = "http://dev.markitondemand.com/MODApis/Api/v2/InteractiveChart/json?parameters=" + OpenCurlyBracket;
		Resource = Resource + Quote + "Normalized" + Quote + Colon + "false" + Comma;
		Resource = Resource + Quote + "NumberOfDays" + Quote + Colon + NumberDays + Comma;
		Resource = Resource + Quote + "DataPeriod" + Quote + Colon + Quote + "Day" + Quote + Comma;
		Resource = Resource + Quote + "Elements" + Quote + Colon + OpenSquareBracket + OpenCurlyBracket + Quote + "Symbol" + Quote + Colon + Quote + Symbol + Quote + Comma;
		Resource = Resource + Quote + "Type" + Quote + Colon + Quote + "price" + Quote + Comma;
		Resource = Resource + Quote + "Params" + Quote + Colon + OpenSquareBracket + Quote + "c" + Quote;
		Resource = Resource + CloseSquareBracket + CloseCurlyBracket + CloseSquareBracket + CloseCurlyBracket;

		//System.out.println(Resource);

		Client c = ClientBuilder.newClient();
		WebTarget target = c.target(Resource);
		Response response = target.request().get();
		
		//System.out.println("Response status is " + response.getStatus());
		
		Currency = "";
		Values = null;
		Dates = null;

		String StrResponse = response.readEntity(String.class);
		//System.out.println("Response is " + StrResponse);

		JSONObject JSONData = null;
		try {
			JSONData = new JSONObject(StrResponse);
			// // System.out.println(JSONData.toString());
		} catch (JSONException e) {
			// System.out.println(e.getMessage());
			return false;
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			return false;
		}

		try {
			Dates = JSONData.getJSONArray("Dates");
			// // System.out.println("Dates (" + Dates.length() + "): " + Dates.toString());
		} catch (JSONException e) {
			// System.out.println(e.getMessage());
			return false;
		}
		/*
			JSONArray Positions = null;
			try {
				Positions = JSONData.getJSONArray("Positions");
				// System.out.println("Positions (" + Positions.length() + "): " + Positions.toString());
			} catch (JSONException e) {
				// System.out.println(e.getMessage());
			}
		 */		
		JSONObject Elements;
		try {
			Elements = JSONData.getJSONArray("Elements").getJSONObject(0);
			// // System.out.println("Elements: " + Elements.toString());
			/*			
				String Type;
				try {
					Type = Elements.getString("Type");
					// System.out.println("Type: " + Type);
				} catch (JSONException e) {
					// System.out.println(e.getMessage());
				}
			 */			
			try {
				Symbol = Elements.getString("Symbol");
				// System.out.println("Symbol: " + Symbol);
			} catch (JSONException e) {
				// System.out.println(e.getMessage());
				return false;
			}

			try {
				Currency = Elements.getString("Currency");
				// System.out.println("Currency: " + Currency);
			} catch (JSONException e) {
				// System.out.println(e.getMessage());
				return false;
			}

			try {
				Values = Elements.getJSONObject("DataSeries").getJSONObject("close").getJSONArray("values");
				// System.out.println("Values (" + Values.length() + ") : " + Values.toString());
			} catch (JSONException e) {
				// System.out.println(e.getMessage());
				return false;
			}

		} catch (JSONException e) {
			// System.out.println(e.getMessage());
			return false;
		}
		
		DataValidated = true;
		return DataValidated;
	}
	
	protected Chart getChart() {

		// OK now we have the data in the format we want it, time to create a chart

		Chart chart = new Chart(ChartType.LINE);
		chart.setWidth("100%");
		chart.setHeight("400px");

		// Modify the default configuration a bit

		Configuration conf = chart.getConfiguration();
		conf.setTitle("Share Price for " + Symbol);
		conf.getLegend().setEnabled(false); // Disable legend

		// Store the data supplied by the RESTful web service in a bean item container

		BeanItemContainer<SharePriceHist> SharePriceHist = new BeanItemContainer<SharePriceHist>(SharePriceHist.class);

		SharePriceHist ShareBean;
		String DateStr = null;
		GregorianCalendar cal = new GregorianCalendar();
		long EpochVal;

		try {
			String FirstDate = Dates.getString(0);
			FirstDate = FirstDate.substring(8, 10) + "/" + FirstDate.substring(5, 7) + "/" + FirstDate.substring(0, 4);
			String LastDate = Dates.getString(Values.length() - 1);
			LastDate = LastDate.substring(8, 10) + "/" + LastDate.substring(5, 7) + "/" + LastDate.substring(0, 4);
			conf.setSubTitle("between " + FirstDate + " and " + LastDate);
		} catch (JSONException e1) {
			// System.out.println(e1.getMessage());
			return null;
		}

		for (int ele = 0; ele < Values.length(); ele++) {
			try {
				DateStr = Dates.getString(ele);
				cal.set(new Integer(DateStr.substring(0, 4)), (new Integer(DateStr.substring(5, 7))) - 1, new Integer(DateStr.substring(8, 10)));
				EpochVal = cal.getTimeInMillis();

				ShareBean = new SharePriceHist(ele);
				ShareBean.setEpoch(EpochVal);
				ShareBean.setPrice(Values.getDouble(ele));
				SharePriceHist.addBean(ShareBean);

				// System.out.println("" + ShareBean.getId() + " : " + DateStr + " : " + ShareBean.getEpoch() + " : " + ShareBean.getPrice());

				ShareBean = null;

			} catch (JSONException e) {
				// System.out.println(e.getMessage());
				return null;
			}
		}

		ContainerDataSeries price = new ContainerDataSeries(SharePriceHist);
		price.setName("Price");
		price.setXPropertyId("epoch");
		price.setYPropertyId("price");

		conf.addSeries(price);
		XAxis xaxis = conf.getxAxis();
		xaxis.setTitle("Date Range");
		xaxis.setType(AxisType.DATETIME);

		// Set the Y axis title

		YAxis yaxis = conf.getyAxis();
		yaxis.setTitle("Price / " + Currency);
		
		// System.out.println("Returning chart");
		return chart;
	}
}
