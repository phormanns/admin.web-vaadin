package de.hsadmin.web;

import java.util.Map;

import com.vaadin.data.Validator;
import com.vaadin.ui.HorizontalLayout;

public class NullEditor extends HorizontalLayout implements IHSEditor {

	private static final long serialVersionUID = 1L;

	@Override
	public void setValues(Map<String, Object> valuesMap) {
		
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public void addValidator(Validator validator) {
		
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
