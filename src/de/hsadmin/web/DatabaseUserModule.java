package de.hsadmin.web;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertySelectValues;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.PacPrefixedNamePropertyFieldFactory;
import de.hsadmin.web.vaadin.PasswordPropertyFieldFactory;

public abstract class DatabaseUserModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public abstract String getModuleIdent();
	
	@Override
	protected void initModule() {
		MainApplication application = getApplication();
		moduleConfig = new ModuleConfig(getModuleIdent(), application.getLocale());
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
		PropertyConfig nameProp = new PropertyConfig(moduleConfig, "name", String.class, new PacPrefixedNamePropertyFieldFactory(this));
		nameProp.setSelectValues(new PropertySelectValues() {
			@Override
			public boolean newItemsAllowed() {
				return false;
			}
			@Override
			public boolean hasSelectList() {
				return true;
			}
			@Override
			public Map<String, String> getSelectValues() {
				List<String> list = getPackets();
				TreeMap<String,String> map = new TreeMap<String, String>();
				for (String pac : list) {
					map.put(pac, pac);
				}
				return map;
			}
		});
		nameProp.setWriteOnce(true);
		PropertyConfig passwordProp = new PropertyConfig(moduleConfig, "password", String.class, PropertyTableColumn.NONE, new PasswordPropertyFieldFactory(this));
		idProp.setShowInForm(false);
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(nameProp);
		moduleConfig.addProperty(passwordProp);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
