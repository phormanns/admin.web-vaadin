package de.hsadmin.web.vaadin;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.Module;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

public class PasswordPropertyFieldFactory implements PropertyFieldFactory {

	private Module module;
	private boolean readOnly;
	private boolean writeOnce;

	public PasswordPropertyFieldFactory(Module module) {
		this.module = module;
	}
	
	@Override
	public Object createFieldComponent(PropertyConfig prop, Object value) {
		ModuleConfig config = module.getModuleConfig();
		VerticalLayout layout = new VerticalLayout();
		layout.setCaption(prop.getLabel());
		layout.setData(prop.getId());
		PasswordField tf1 = new PasswordField(config.getLabel(prop.getId() + "1"));
		tf1.setData(prop.getId());
		tf1.setWidth(480.0f, Sizeable.UNITS_PIXELS);
		tf1.setValue(value != null ? value : prop.getDefaultValue());
		tf1.setReadOnly(readOnly);
		layout.addComponent(tf1);
		PasswordField tf2 = new PasswordField(config.getLabel(prop.getId() + "2"));
		tf2.setData(prop.getId());
		tf2.setWidth(480.0f, Sizeable.UNITS_PIXELS);
		tf2.setValue(value != null ? value : prop.getDefaultValue());
		tf2.setReadOnly(readOnly);
		layout.addComponent(tf2);
		return layout;
	}

	@Override
	public String getValue(PropertyConfig prop, Object component) throws HsarwebException {
		if (component instanceof VerticalLayout) {
			VerticalLayout layout = (VerticalLayout) component;
			PasswordField pw1 = (PasswordField) layout.getComponent(0);
			PasswordField pw2 = (PasswordField) layout.getComponent(1);
			if (pw1.getValue().equals(pw2.getValue())) {
				return (String) pw1.getValue();
			} else {
				throw new HsarwebException("password mismatch");
			}
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
