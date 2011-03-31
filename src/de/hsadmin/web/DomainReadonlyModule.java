package de.hsadmin.web;

import java.util.Date;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyTableColumn;

public class DomainReadonlyModule extends AbstractModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("domain", getApplication().getLocale());
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "name", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "user", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "hive", String.class, PropertyTableColumn.HIDDEN));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "since", Date.class));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
