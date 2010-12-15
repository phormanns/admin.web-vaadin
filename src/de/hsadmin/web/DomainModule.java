package de.hsadmin.web;

import java.util.Date;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;

public class DomainModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public DomainModule() {
		moduleConfig = new ModuleConfig("domain");
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, "", true, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "name", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "user", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "pac", String.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "hive", String.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "since", Date.class, ""));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
