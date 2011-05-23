package de.hsadmin.web;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertySelectValues;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.PasswordPropertyFieldFactory;
import de.hsadmin.web.vaadin.SelectPropertyFieldFactory;

public abstract class DatabaseUserModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public abstract String getModuleIdent();
	
	@Override
	protected void initModule() {
		MainApplication application = getApplication();
		moduleConfig = new ModuleConfig(getModuleIdent(), application.getLocale());
		String login = application.getLogin();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
		PropertyConfig nameProp = new PropertyConfig(moduleConfig, "name", String.class);
		nameProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				if (pac.length() >= 5) {
					return pac + "_";
				}
				return "";
			}
		});
		nameProp.setWriteOnce(true);
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN, new SelectPropertyFieldFactory());
		pacProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac;
			}
		});
		pacProp.setSelectValues(new PropertySelectValues() {
			@Override
			public boolean newItemsAllowed() {
				return false;
			}
			@Override
			public boolean hasSelectList() {
				return true;
			}
			@Override
			public Map<String, String> getSelectValues() {
				List<String> list = getPackets();
				TreeMap<String,String> map = new TreeMap<String, String>();
				for (String pac : list) {
					map.put(pac, pac);
				}
				return map;
			}
		});
		pacProp.setWriteOnce(true);
		PropertyConfig passwordProp = new PropertyConfig(moduleConfig, "password", String.class, PropertyTableColumn.NONE, new PasswordPropertyFieldFactory(this));
		idProp.setShowInForm(false);
		pacProp.setShowInForm(false);
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(pacProp);
		moduleConfig.addProperty(nameProp);
		moduleConfig.addProperty(passwordProp);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
