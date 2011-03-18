package de.hsadmin.web;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertyFormField;
import de.hsadmin.web.config.PropertyTableColumn;

public class EMailAliasModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("emailalias");
		String login = getApplication().getLogin();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY, PropertyFormField.INTERNAL_KEY));
		PropertyConfig nameProp = new PropertyConfig(moduleConfig, "name", String.class);
		nameProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				if (pac.length() >= 5) {
					return pac + "-";
				}
				return "";
			}
		});
		moduleConfig.addProperty(nameProp);
		PropertyConfig targetProp = new PropertyConfig(moduleConfig, "target", String.class);
		targetProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				if (pac.length() >= 5) {
					return pac + "-";
				}
				return "";
			}
		});
		moduleConfig.addProperty(targetProp);
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN, PropertyFormField.READONLY);
		pacProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac;
			}
		});
		moduleConfig.addProperty(pacProp);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
