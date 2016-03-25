package de.hsadmin.web;

import java.util.Map;


public class HSPacPrefixedField extends HSTextField implements PacNamePrefixed {

	private static final long serialVersionUID = 1L;

	private final char delimiter;
	
	private String pacName;

	public HSPacPrefixedField(final String name, final char delimiter) {
		super(name);
		pacName = "xyz00";
		this.delimiter = delimiter;
		addValidator(new PacNamePrefixValidator(this));
	}

	@Override
	public String getPacName() {
		return pacName;
	}

	@Override
	public void setValues(final Map<String, Object> valuesMap) {
		super.setValues(valuesMap);
		pacName = (String) valuesMap.get("pac");
		final String currentValue = getValue();
		if (currentValue == null || currentValue.isEmpty()) {
			setValue(pacName + delimiter);
		}
	}
}
