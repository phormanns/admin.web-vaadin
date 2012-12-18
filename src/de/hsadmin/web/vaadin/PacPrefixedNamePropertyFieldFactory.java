package de.hsadmin.web.vaadin;

import java.util.Map;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.Module;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;


public class PacPrefixedNamePropertyFieldFactory implements PropertyFieldFactory {

	final private Module module;

	private boolean readOnly = false;
	private boolean writeOnce = false;

	public PacPrefixedNamePropertyFieldFactory(Module module) {
		this.module = module;
	}

	@Override
	public Object createFieldComponent(PropertyConfig prop, Object value) {
		ModuleConfig config = module.getModuleConfig();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setCaption(prop.getLabel());
		layout.setData(prop.getId());
		Select sel = new Select(config.getLabel(prop.getId() + ".pacprefix"));
		sel.setData(prop.getId());
		sel.setNullSelectionAllowed(false);
		sel.setNewItemsAllowed(prop.newItemsAllowed());
		Map<String, String> selectValues = prop.getSelectValues();
		for (Object key : selectValues.keySet()) {
			sel.addItem(key);
			sel.setItemCaption(key, selectValues.get(key));
		}
		sel.setWidth(80.0f, Sizeable.UNITS_PIXELS);
		sel.setInvalidAllowed(prop.newItemsAllowed());
		layout.addComponent(sel);
		
		TextField separator = new TextField(" ", config.getLabel(prop.getId() + ".separator"));
		separator.setWidth(18.0f, Sizeable.UNITS_PIXELS);
		separator.setReadOnly(true);
		layout.addComponent(separator);
		
		TextField tf = new TextField(config.getLabel(prop.getId() + ".postfix"));
		tf.setData(prop.getId());
		tf.setWidth(384.0f, Sizeable.UNITS_PIXELS);
		layout.addComponent(tf);
		String valueOrDefault = (value != null && value instanceof String) ? ((String) value) : prop.getDefaultValue();
		if (valueOrDefault.length() >= 5) {
			sel.setValue(valueOrDefault.substring(0, 5));
			tf.setValue(valueOrDefault.length() > 6 ? valueOrDefault.substring(6) : "");
			sel.setReadOnly(readOnly || writeOnce);
			tf.setReadOnly(readOnly || writeOnce);
		} else {
			sel.setReadOnly(readOnly);
			tf.setReadOnly(readOnly);
		}
		return layout;
	}

	@Override
	public String getValue(PropertyConfig prop, Object component) throws HsarwebException {
		ModuleConfig config = module.getModuleConfig();
		HorizontalLayout layout = (HorizontalLayout) component;
		Select sel = (Select) layout.getComponent(0);
		TextField tf = (TextField) layout.getComponent(2);
		String pacPart = (String) sel.getValue();
		String userPart = (String) tf.getValue();
		return pacPart + config.getLabel(prop.getId() + ".separator") + userPart;
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
