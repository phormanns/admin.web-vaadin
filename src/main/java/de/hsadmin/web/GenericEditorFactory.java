package de.hsadmin.web;

import java.io.Serializable;

import com.vaadin.data.Validator;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.enums.ReadWritePolicy;

public class GenericEditorFactory extends AbstractEditorFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public IHSEditor getEditor(final String action, final PropertyInfo propertyInfo, final HSAdminSession session) 
	{
		final String inputName = propertyInfo.getName();
		if ("password".equals(inputName)) {
			return getPasswordField(action, propertyInfo);
		}
		if ("shell".equals(inputName)) {
			return getShellSelect(action, propertyInfo);
		}
		final String module = propertyInfo.getModule();
		if ("user".equals(module)) {
			if ("name".equals(inputName)) {
				return getPacPrefixedField(action, propertyInfo);
			}
		}
		return getTextField(action, propertyInfo);
	}

	private IHSEditor getPacPrefixedField(final String action, final PropertyInfo propertyInfo) {
		final HSPacPrefixedField field = new HSPacPrefixedField(propertyInfo.getName());
		field.setWidth("100%");
		field.setValue("xyz00-");
		enableAndValidate(action, propertyInfo, field);
		return field;
	}

	private IHSEditor getShellSelect(final String action, final PropertyInfo propertyInfo) {
		final HSShellSelect field = new HSShellSelect(propertyInfo.getName());
		field.setWidth("100%");
		field.setEnabled("new".equals(action) || "edit".equals(action));
		return field;
	}

	private IHSEditor getPasswordField(final String action, final PropertyInfo propertyInfo) {
		final HSPasswordField field = new HSPasswordField(propertyInfo.getName());
		field.setWidth("100%");
		field.setEnabled("new".equals(action) || "edit".equals(action));
		return field;
	}

	private IHSEditor getTextField(final String action, final PropertyInfo propertyInfo) {
		final HSTextField field = new HSTextField(propertyInfo.getName());
		field.setWidth("100%");
		enableAndValidate(action, propertyInfo, field);
		return field;
	}

	private void enableAndValidate(final String action,
			final PropertyInfo propertyInfo, final IHSEditor field) {
		if (isWriteAble(propertyInfo, action)) {
			final String regexp = propertyInfo.getValidationRegexp();
			final int minLength = propertyInfo.getMinLength();
			final int maxLength = propertyInfo.getMaxLength();
			field.addValidator(new Validator() {
				private static final long serialVersionUID = 1L;
				@Override
				public void validate(Object value) throws InvalidValueException {
					final String inputString = (String) value;
					if (inputString == null || !inputString.matches(regexp)) {
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
	}

	private boolean isWriteAble(PropertyInfo propertyInfo, String action) {
		if ("view".equals(action)) {
			return false;
		}
		return (ReadWritePolicy.WRITEONCE.equals(propertyInfo.getReadwriteable()) && action.equals("new")) ||
				 (ReadWritePolicy.READWRITE.equals(propertyInfo.getReadwriteable()) && ( action.equals("edit") || action.equals("new") ));
	}

}
