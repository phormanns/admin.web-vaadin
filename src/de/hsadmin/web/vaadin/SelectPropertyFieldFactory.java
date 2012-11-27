package de.hsadmin.web.vaadin;

import java.util.Map;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Select;

import de.hsadmin.web.AbstractProperty;
import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.StringProperty;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

public class SelectPropertyFieldFactory implements PropertyFieldFactory {

	private boolean readOnly = false;
	private boolean writeOnce = false;

	@Override
	public Object createFieldComponent(PropertyConfig prop, Object value) {
		Select sel = new Select(prop.getLabel());
		sel.setData(prop.getId());
		sel.setNullSelectionAllowed(false);
		sel.setNewItemsAllowed(prop.newItemsAllowed());
		Map<String, String> selectValues = prop.getSelectValues();
		for (Object key : selectValues.keySet()) {
			sel.addItem(key);
			sel.setItemCaption(key, selectValues.get(key));
		}
		sel.setWidth(480.0f, Sizeable.UNITS_PIXELS);
		sel.setValue(value != null ? value : prop.getDefaultValue());
		sel.setReadOnly(readOnly);
		sel.setInvalidAllowed(prop.newItemsAllowed());
		return sel;
	}

	@Override
	public AbstractProperty getValue(PropertyConfig prop, Object component)
			throws HsarwebException {
		if (component instanceof Select) {
			return new StringProperty((String) ((Select) component).getValue());
		}
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
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
