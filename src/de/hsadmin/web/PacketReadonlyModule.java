package de.hsadmin.web;

import java.util.Date;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.DatePropertyFieldFactory;

public class PacketReadonlyModule extends AbstractModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;
	
	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("pac", getApplication().getLocale());
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "name", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "basepac", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "components", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "hive", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "curinetaddr", String.class));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "created", Date.class, new DatePropertyFieldFactory()));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "customer", String.class, PropertyTableColumn.HIDDEN));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
