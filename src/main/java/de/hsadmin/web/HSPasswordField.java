package de.hsadmin.web;

import java.util.Map;

import com.vaadin.ui.PasswordField;

public class HSPasswordField extends PasswordField implements IHSEditor {

	private static final long serialVersionUID = 1L;
	
	public HSPasswordField(final String name) {
		super(I18N.getText(name));
	}

	@Override
	public void setValues(final Map<String, Object> valuesMap) {
		setValue("");
	}

}
