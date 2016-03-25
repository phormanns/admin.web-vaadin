package de.hsadmin.web;

import com.vaadin.data.Validator;

public class PacNamePrefixValidator implements Validator {

	private static final long serialVersionUID = 1L;
	
	private final PacNamePrefixed pacNameProvider;

	public PacNamePrefixValidator(final PacNamePrefixed pacPrefixed) {
		this.pacNameProvider = pacPrefixed;
	}

	@Override
	public void validate(final Object value) throws InvalidValueException {
		final String pacName = pacNameProvider.getPacName();
		if (value == null || !(value instanceof String) || !(((String )value).startsWith(pacName))) {
			throw new InvalidValueException("name must start with package name \"" + pacName + "\"");
		}
	}

}
