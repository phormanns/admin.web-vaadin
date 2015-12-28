package de.hsadmin.web;

import java.util.Map;


public class HSPacPrefixedField extends HSTextField implements PacNamePrefixed {

	private static final long serialVersionUID = 1L;

	private String pacName;

	public HSPacPrefixedField(final String name) {
		super(name);
		pacName = "xyz00";
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
	}
}
