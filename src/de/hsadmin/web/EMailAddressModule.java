package de.hsadmin.web;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;

public class EMailAddressModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public EMailAddressModule() {
		moduleConfig = new ModuleConfig("emailaddress");
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, true, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "emailaddress", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "localpart", String.class, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "subdomain", String.class, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "domain", String.class, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "target", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "admin", String.class, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "pac", String.class, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "fulldomain", String.class, true));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
