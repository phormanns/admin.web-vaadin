package de.hsadmin.web;

import java.util.Map;

import com.vaadin.ui.TextField;

public class HSTextField extends TextField implements IHSEditor {

	private static final long serialVersionUID = 1L;
	
	private final String propertyName;

	public HSTextField(final String name) {
		super(I18N.getText(name));
		this.propertyName = name;
	}

	@Override
	public void setValues(final Map<String, Object> valuesMap) {
		String newValue = "";
		final Object valObject = valuesMap.get(propertyName);
		if (valObject instanceof String) {
			newValue = (String) valObject;
		}
		if (valObject instanceof Integer) {
			((Integer) valObject).toString();
		}
		if (valObject instanceof Boolean) {
			((Boolean) valObject).toString();
		}
		if (newValue != null) {
			setValue(newValue);
		}
	}

}
