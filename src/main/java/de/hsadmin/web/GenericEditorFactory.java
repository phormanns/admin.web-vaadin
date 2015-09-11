package de.hsadmin.web;

import com.vaadin.data.Validator;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.enums.ReadWritePolicy;

public class GenericEditorFactory extends AbstractEditorFactory {

	@Override
	public IHSEditor getEditor(final String action, final PropertyInfo propertyInfo, final String inputName, final HSAdminSession session) {
		
		final IHSEditor field = "password".equals(inputName) ? getPasswordField(action, propertyInfo, inputName) : getTextField(action, propertyInfo, inputName);
		return field;
	}

	private IHSEditor getPasswordField(String action, PropertyInfo propertyInfo, String inputName) {
		final HSPasswordField field = new HSPasswordField(inputName);
		field.setWidth("100%");
		field.setEnabled("new".equals(action) || "edit".equals(action));
		return field;
	}

	private IHSEditor getTextField(final String action, final PropertyInfo propertyInfo, final String inputName) {
		final HSTextField field = new HSTextField(inputName);
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
		return (ReadWritePolicy.WRITEONCE.equals(propertyInfo.getReadwriteable()) && action.equals("new")) ||
				 (ReadWritePolicy.READWRITE.equals(propertyInfo.getReadwriteable()) && ( action.equals("edit") || action.equals("new") ));
	}

}
