package de.hsadmin.web;

import java.util.Date;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;

public class QueueTaskModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public QueueTaskModule() {
		moduleConfig = new ModuleConfig("q");
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, "", true, true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "title", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "status", String.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "started", Date.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "finished", Date.class, ""));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "user", String.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "details", String.class, "", true));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "exception", String.class, "", true));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
