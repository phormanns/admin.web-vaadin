package de.hsadmin.web.vaadin;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.TextField;

import de.hsadmin.web.AbstractProperty;
import de.hsadmin.web.StringProperty;
import de.hsadmin.web.XmlrpcProperty;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

public class DefaultPropertyFieldFactory implements PropertyFieldFactory {

	private boolean readOnly = false;
	private boolean writeOnce = false;
	
	@Override
	public Object createFieldComponent(PropertyConfig prop, XmlrpcProperty value) {
		TextField tf = new TextField(prop.getLabel());
		tf.setData(prop.getId());
		tf.setWidth(480.0f, Sizeable.UNITS_PIXELS);
		String valueOrDefault = prop.getDefaultValue();
		if (value instanceof AbstractProperty) {
			valueOrDefault = ((AbstractProperty) value).toStringValue();
		}
		tf.setValue(valueOrDefault);
		tf.setReadOnly(isReadOnly());
		return tf;
	}

	@Override
	public AbstractProperty getValue(PropertyConfig prop, Object component) {
		if (component instanceof TextField) {
			return new StringProperty((String) ((TextField) component).getValue());
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
