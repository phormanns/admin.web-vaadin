package de.hsadmin.web;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.vaadin.data.Validator;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.enums.ReadWritePolicy;

public class GenericEditorFactory extends AbstractEditorFactory implements Serializable {

	private static final long serialVersionUID = 1L;
	ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages");

	@Override
	public IHSEditor getEditor(final String action, final PropertyInfo propertyInfo, final String inputName, final HSAdminSession session) {
		
		final IHSEditor field = "password".equals(inputName) ? getPasswordField(action, propertyInfo, inputName) : getTextField(action, propertyInfo, inputName);
		return field;
	}

	private IHSEditor getPasswordField(String action, PropertyInfo propertyInfo, String inputName) {
		String text;
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			text = resourceBundle.getString(inputName);
		}catch(MissingResourceException e){
			text = inputName;
		}
		final HSPasswordField field = new HSPasswordField(text);
		field.setWidth("100%");
		field.setEnabled("new".equals(action) || "edit".equals(action));
		return field;
	}

	private IHSEditor getTextField(final String action, final PropertyInfo propertyInfo, final String inputName) {
		String text;
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			text = resourceBundle.getString(inputName);
		}catch(MissingResourceException e){
			text = inputName;
		}
		
		final HSTextField field = new HSTextField(text);
		field.setWidth("100%");
		if (isWriteAble(propertyInfo, action)) {
			final String regexp = propertyInfo.getValidationRegexp();
			final int minLength = propertyInfo.getMinLength();
			final int maxLength = propertyInfo.getMaxLength();
			field.addValidator(new Validator() {
				private static final long serialVersionUID = 1L;
				@Override
				public void validate(Object value) throws InvalidValueException {
					final String inputString = (String) value;
					if (!inputString.matches(regexp)) {
						throw new InvalidValueException("input must match " + regexp);
					}
					if (inputString.length() < minLength) {
						throw new InvalidValueException("minimal length is " + minLength);
					}
					if (inputString.length() > maxLength) {
						throw new InvalidValueException("maximal length is " + maxLength);
					}
				}
			});
		} else {
			field.setEnabled(false);
		}
		return field;
	}

	private boolean isWriteAble(PropertyInfo propertyInfo, String action) {
		if ("view".equals(action)) {
			return false;
		}
		return (ReadWritePolicy.WRITEONCE.equals(propertyInfo.getReadwriteable()) && action.equals("new")) ||
				 (ReadWritePolicy.READWRITE.equals(propertyInfo.getReadwriteable()) && ( action.equals("edit") || action.equals("new") ));
	}

}
