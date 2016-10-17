package org.bluemix.challenge;

import org.bluemix.challenge.backend.Customer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;

public class MyConverterFactory extends DefaultConverterFactory {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL>
	createConverter(Class<PRESENTATION> presentationType,
			Class<MODEL> modelType) {
		// Handle one particular type conversion
		if (String.class == presentationType && Customer.Gender.class == modelType)
			return ((Converter<PRESENTATION, MODEL>) new GenderConverter());
		// Default to the supertype
		return super.createConverter(presentationType,
				modelType);
	}
}
