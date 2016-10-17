package org.bluemix.challenge;

import java.util.Locale;
import org.bluemix.challenge.backend.Customer;
import com.vaadin.data.util.converter.Converter;

public class GenderConverter implements Converter<String, Customer.Gender> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public Class<Customer.Gender> getModelType() {
		return Customer.Gender.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

	public Customer.Gender convertToModel(String value, Class<? extends Customer.Gender> targetType, Locale locale) throws ConversionException {

		if (value == null) {
			return null;
		} else if (value.equals("Male")) {
			return Customer.Gender.Male;
		} else if (value.equals("Female")) {
			return Customer.Gender.Female;
		} else if (value.equals("Other")) {
			return Customer.Gender.Other;
		} else {
			return null;
		}
	}

	@Override
	public String convertToPresentation(Customer.Gender value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (value == null) {
			return null;
		} else if (value.equals(Customer.Gender.Male)) {
			return "Male";
		} else if (value.equals(Customer.Gender.Female)) {
			return "Female";
		} else if (value.equals(Customer.Gender.Other)) {
			return "Other";
		} else {
			return null;
		}
	}
}
