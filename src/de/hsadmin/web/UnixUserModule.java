package de.hsadmin.web;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;

public class UnixUserModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public UnixUserModule() {
		moduleConfig = new ModuleConfig("user");
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, "", true, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "name", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "comment", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "shell", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "userid", Long.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "homedir", String.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "pac", String.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "quota_softlimit", Long.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "quota_hardlimit", Long.class, "", true));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
