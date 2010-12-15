package de.hsadmin.web;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;

public class EMailAliasModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public EMailAliasModule() {
		moduleConfig = new ModuleConfig("emailalias");
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, "", true, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "name", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "target", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "pac", String.class, "", true));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
