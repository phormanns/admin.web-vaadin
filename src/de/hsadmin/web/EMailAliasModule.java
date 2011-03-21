package de.hsadmin.web;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertyTableColumn;

public class EMailAliasModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("emailalias");
		String login = getApplication().getLogin();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
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
		nameProp.setWriteOnce(true);
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
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN);
		pacProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac;
			}
		});
		pacProp.setReadOnly(true);
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(nameProp);
		moduleConfig.addProperty(targetProp);
		moduleConfig.addProperty(pacProp);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
