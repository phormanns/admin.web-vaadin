package de.hsadmin.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertySelectValues;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.SelectPropertyFieldFactory;

public class DomainModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("domain");
		String login = getApplication().getLogin();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
		PropertyConfig nameProp = new PropertyConfig(moduleConfig, "name", String.class);
		nameProp.setWriteOnce(true);
		PropertyConfig userProp = new PropertyConfig(moduleConfig, "user", String.class, new SelectPropertyFieldFactory());
		userProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac;
			}
		});
		userProp.setSelectValues(new PropertySelectValues() {
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
				List<String> list = getUsers();
				TreeMap<String,String> map = new TreeMap<String, String>();
				for (String usr : list) {
					map.put(usr, usr);
				}
				return map;
			}
		});
		userProp.setWriteOnce(true);
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN);
		pacProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac;
			}
		});
		pacProp.setReadOnly(true);
		PropertyConfig hiveProp = new PropertyConfig(moduleConfig, "hive", String.class, PropertyTableColumn.HIDDEN);
		hiveProp.setReadOnly(true);
		PropertyConfig sinceProp = new PropertyConfig(moduleConfig, "since", Date.class);
		sinceProp.setReadOnly(true);
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(nameProp);
		moduleConfig.addProperty(userProp);
		moduleConfig.addProperty(pacProp);
		moduleConfig.addProperty(hiveProp);
		moduleConfig.addProperty(sinceProp);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

	public List<String> getUsers() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Object callSearch = getApplication().getRemote().callSearch("user", new HashMap<String, String>());
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					if (row instanceof Map<?, ?>) {
						Object object = ((Map<?, ?>) row).get("name");
						if (object instanceof String) {
							list.add((String) object);
						}
					}
				}
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
			getApplication().showSystemException(e);
		}
		return list;
	}

}
