package de.hsadmin.web;

import java.util.List;
import java.util.Map;

import com.vaadin.ui.NativeSelect;

public class HSSelect extends NativeSelect implements IHSEditor {

	private static final long serialVersionUID = 1L;
	
	private final String propertyName;

	public HSSelect(final String label, final List<String> values) {
		this(label, 0, values);
	}
	
	public HSSelect(final String label, final int defaultIndex, final List<String> values) {
		super(I18N.getText(label));
		this.propertyName = label;
		super.addItems(values);
		if (values != null && values.size() > defaultIndex) {
			super.setValue(values.get(defaultIndex));
		}
		super.setMultiSelect(false);
		super.setNullSelectionAllowed(false);
	}

	@Override
	public void setValues(final Map<String, Object> valuesMap) {
		final Object newValue = valuesMap.get(propertyName);
		if (newValue != null) {
			setValue(newValue);
		}
	}
}
