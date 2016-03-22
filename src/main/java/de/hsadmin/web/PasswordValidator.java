package de.hsadmin.web;

import com.vaadin.data.Validator;

public class PasswordValidator implements Validator {

	private static final int MIN_PASSWORD_LEN = 6;

	private static final long serialVersionUID = 1L;

	private final boolean required;

	public PasswordValidator(final boolean passwordRequired) {
		this.required = passwordRequired;
	}
	
	@Override
	public void validate(final Object value) throws InvalidValueException {
		if (value == null) {
			if (required) {
				throw new InvalidValueException("password required");
			} else {
				return;
			}
		}
		final String password = (String) value;
		if (password.isEmpty()) {
			if (required) {
				throw new InvalidValueException("password required");
			} else {
				return;
			}
		}
		if (password.length() < MIN_PASSWORD_LEN) {
			throw new InvalidValueException("minimal password length is " + MIN_PASSWORD_LEN + " characters");
		}
		int hasLowerCaseChar = 0;
		int hasUpperCaseChar = 0;
		int hasDigits = 0;
		int hasSpecialChar = 0;
		for (int idx = 0; idx < password.length(); idx++) {
			final char test = password.charAt(idx);
			final int type = Character.getType(test);
			if (type == Character.DECIMAL_DIGIT_NUMBER) {
				hasDigits = 1;
			} else {
			if (type == Character.LOWERCASE_LETTER) {
				hasLowerCaseChar = 1;
			} else
			if (type == Character.UPPERCASE_LETTER) {
				hasUpperCaseChar = 1;
			} else
				hasSpecialChar = 1;
			}
		}
		if (hasDigits + hasLowerCaseChar + hasUpperCaseChar + hasSpecialChar < 3) {
			throw new InvalidValueException("a password requires 3 out of 4 "
					+ "different character types: lowercase, uppercase, digits and special characters");
		}
	}

}
