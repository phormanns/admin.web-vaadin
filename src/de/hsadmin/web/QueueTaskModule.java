package de.hsadmin.web;

import java.util.Date;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.DatePropertyFieldFactory;

public class QueueTaskModule extends AbstractModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("q", getApplication().getLocale());
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY));
		PropertyConfig titleProp = new PropertyConfig(moduleConfig, "title", String.class);
		titleProp.setExpandRatio(0.7f);
		moduleConfig.addProperty(titleProp);
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "status", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "started", Date.class, new DatePropertyFieldFactory()));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "finished", Date.class, new DatePropertyFieldFactory()));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "user", String.class, PropertyTableColumn.HIDDEN));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "details", String.class, PropertyTableColumn.HIDDEN));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "exception", String.class, PropertyTableColumn.HIDDEN));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
