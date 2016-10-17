package org.bluemix.challenge;

import org.bluemix.challenge.backend.Customer;
import org.bluemix.challenge.backend.DummyDataService;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class GridView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private Customer CurrentCustomer;
	final private BeanFieldGroup<Customer> FldGrp = new BeanFieldGroup<Customer>(Customer.class);
	private TextField idFld;

	public GridView() {

		this.setMargin(true);
		this.setSpacing(true);

		// Use this factory globally in the application to allow Gender to String conversion
		UI.getCurrent().getSession().setConverterFactory(new MyConverterFactory());

		// Get data from dummy data service        
		new DummyDataService();        
		DummyDataService dds = DummyDataService.createDemoService();        

		List<Customer> CustomerList = dds.findAll();
		//Notification.show("There are " + CustomerList.size() + " customers");

		// Convert the List to a BeanItemContainer

		final BeanItemContainer<Customer> CustomerContainer = new BeanItemContainer<Customer>(Customer.class, CustomerList);
		CustomerContainer.sort(new Object[] {"firstName", "lastName"}, new boolean[] {true, true});

		final Grid grid = new Grid("Customers", CustomerContainer); 
		//System.out.println(grid.getColumns());
		grid.setColumns("firstName", "lastName", "birthDate", "email", "phone");

		Grid.Column bornColumn = grid.getColumn("birthDate");
		bornColumn.setRenderer(new DateRenderer("%1$td %1$tb %1$tY"));
		bornColumn.setHeaderCaption("Date of Birth");

		grid.setWidth("750px");
		grid.setHeight("300px");

		// Create a header row to hold column filters
		HeaderRow filterRow = grid.appendHeaderRow();

		// Set up a filter for the first name
		HeaderCell FirstNameFilter = filterRow.getCell("firstName");

		// Have an input field to use for filter
		TextField FirstNameFilterFld = new TextField();
		FirstNameFilterFld.setImmediate(true);
		FirstNameFilterFld.setWidth("70px");

		//On Change of text, filter the data of the grid
		FirstNameFilterFld.addTextChangeListener(new TextChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {

				String newValue = (String) event.getText();

				// This is important, this removes the previous filter
				// that was used to filter the container
				CustomerContainer.removeContainerFilters("firstName");

				if (null != newValue && !newValue.isEmpty()) {
					CustomerContainer.addContainerFilter(new SimpleStringFilter(
							"firstName", newValue, true, false));
				}
			}        	
		});

		FirstNameFilter.setComponent(FirstNameFilterFld);

		// Set up a filter for the first name
		HeaderCell LastNameFilter = filterRow.getCell("lastName");

		// Have an input field to use for filter
		TextField LastNameFilterFld = new TextField();
		LastNameFilterFld.setImmediate(true);
		LastNameFilterFld.setWidth("70px");

		//On Change of text, filter the data of the grid
		LastNameFilterFld.addTextChangeListener(new TextChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {

				String newValue = (String) event.getText();

				// This is important, this removes the previous filter
				// that was used to filter the container
				CustomerContainer.removeContainerFilters("lastName");

				if (null != newValue && !newValue.isEmpty()) {
					CustomerContainer.addContainerFilter(new SimpleStringFilter(
							"lastName", newValue, true, false));
				}
			}        	
		});

		LastNameFilter.setComponent(LastNameFilterFld);

		// Create two buttons for the form that will be below the grid

		final Button SaveBtn = new Button("Save");
		final Button CancelBtn = new Button("Cancel");

		// Add selection listener

		grid.addSelectionListener(new SelectionListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void select(SelectionEvent event) {

				// Get selection from the selection model
				ArrayList<?> selected = (ArrayList<?>) grid.getSelectionModel().getSelectedRows();
				if (selected != null) {
					CurrentCustomer = (Customer) selected.get(0);
					FldGrp.setItemDataSource(CurrentCustomer);
					idFld.setReadOnly(true);

					SaveBtn.setEnabled(true);
					CancelBtn.setEnabled(true);
				}		
			}     	
		});

		this.addComponent(grid);

		//System.out.println(grid.getContainerDataSource().getContainerPropertyIds());

		FormLayout formLayout = new FormLayout();
		formLayout.setWidth("750px");
		formLayout.setMargin(true);
		formLayout.setSpacing(true);

		Panel panel = new Panel("Customer Maintenance");
		panel.setContent(formLayout);
		panel.setWidth("-1px");

		TextField fld;

		fld = (TextField) FldGrp.buildAndBind("First Name", "firstName");
		fld.setRequired(true);
		formLayout.addComponent(fld);	

		fld = (TextField) FldGrp.buildAndBind("Last Name", "lastName");
		fld.setRequired(true);
		formLayout.addComponent(fld);

		OptionGroup radiobutton = new OptionGroup("Gender");

		radiobutton.addItem(Customer.Gender.Male);
		radiobutton.setItemCaption(Customer.Gender.Male, "Male");
		radiobutton.addItem(Customer.Gender.Female);
		radiobutton.setItemCaption(Customer.Gender.Female, "Female");
		radiobutton.addItem(Customer.Gender.Other);
		radiobutton.setItemCaption(Customer.Gender.Other, "Other");

		radiobutton.setMultiSelect(false);
		FldGrp.bind(radiobutton, "gender");
		formLayout.addComponent(radiobutton);		

		formLayout.addComponent(FldGrp.buildAndBind("Date of Birth", "birthDate"));
		formLayout.addComponent(FldGrp.buildAndBind("Email Address", "email"));
		formLayout.addComponent(FldGrp.buildAndBind("Phone Number", "phone"));		

		fld = (TextField) FldGrp.buildAndBind("Address", "address");
		fld.setNullRepresentation("");
		formLayout.addComponent(fld);

		fld = (TextField) FldGrp.buildAndBind("Town/City", "city");
		fld.setNullRepresentation("");
		formLayout.addComponent(fld);

		fld = (TextField) FldGrp.buildAndBind("Zip Code", "zipCode");
		fld.setNullRepresentation("");
		formLayout.addComponent(fld);

		idFld = (TextField) FldGrp.buildAndBind("Customer ID", "id");
		formLayout.addComponent(idFld);

		// A button to commit the buffer

		SaveBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					if (!FldGrp.isValid()) {
						Notification.show("Customer invalid");
					} else {
						FldGrp.commit();
						Notification.show("Customer saved");

						SaveBtn.setEnabled(false);
						CancelBtn.setEnabled(false);

						// This refreshes the grid cache

						grid.setCellStyleGenerator(grid.getCellStyleGenerator());
					}
				} catch (CommitException e) {
					String Message = "Customer NOT saved";
					if (!e.getMessage().equals("")) {
						Message = Message + " " + e.getMessage();
					}
					Notification.show(Message);
				}				
			}
		});

		SaveBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		SaveBtn.setEnabled(false);
		formLayout.addComponent(SaveBtn);

		// A button to roll back the buffer

		CancelBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				FldGrp.discard();
				Notification.show("Edits cancelled");				
			}
		});

		CancelBtn.setEnabled(false);
		formLayout.addComponent(CancelBtn);

		this.addComponent(panel);
	}
}
