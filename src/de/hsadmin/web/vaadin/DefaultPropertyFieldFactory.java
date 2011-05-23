package de.hsadmin.web.vaadin;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.TextField;

import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

public class DefaultPropertyFieldFactory implements PropertyFieldFactory {

	private boolean readOnly = false;
	private boolean writeOnce = false;
	
	@Override
	public Object createFieldComponent(PropertyConfig prop, Object value) {
		TextField tf = new TextField(prop.getLabel());
		tf.setData(prop.getId());
		tf.setWidth(480.0f, Sizeable.UNITS_PIXELS);
		tf.setValue(value != null ? value : prop.getDefaultValue());
		tf.setReadOnly(isReadOnly());
		return tf;
	}

	@Override
	public String getValue(PropertyConfig prop, Object component) {
		if (component instanceof TextField) {
			return (String) ((TextField) component).getValue();
		}
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setWriteOnce(boolean writeOnce) {
		this.writeOnce = writeOnce;
	}

	@Override
	public boolean isWriteOnce() {
		return writeOnce;
	}

}
