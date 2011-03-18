package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertyFormField;
import de.hsadmin.web.config.PropertySelectValues;
import de.hsadmin.web.config.PropertyTableColumn;

public class EMailAddressModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("emailaddress");
		String login = getApplication().getLogin();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY, PropertyFormField.INTERNAL_KEY));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "emailaddress", String.class, PropertyFormField.NONE));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "localpart", String.class, PropertyTableColumn.HIDDEN, PropertyFormField.WRITEONCE));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "subdomain", String.class, PropertyTableColumn.HIDDEN, PropertyFormField.WRITEONCE));
		PropertyConfig DomainProp = new PropertyConfig(moduleConfig, "domain", String.class, PropertyTableColumn.HIDDEN, PropertyFormField.WRITEONCE);
		DomainProp.setSelectValues(new PropertySelectValues() {
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
				Map<String, String> map = new TreeMap<String, String>();
				List<String> list = getDomains();
				for (String dom : list) {
					map.put(dom, dom);
				}
				return map;
			}
		});
		moduleConfig.addProperty(DomainProp);
		PropertyConfig targetProp = new PropertyConfig(moduleConfig, "target", String.class);
		targetProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac + "-";
			}
		});
		moduleConfig.addProperty(targetProp);
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "admin", String.class, PropertyTableColumn.HIDDEN, PropertyFormField.NONE));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN, PropertyFormField.NONE));
		moduleConfig.addProperty(new PropertyConfig(moduleConfig, "fulldomain", String.class, PropertyTableColumn.HIDDEN, PropertyFormField.NONE));
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

	public List<String> getDomains() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Object callSearch = getApplication().getRemote().callSearch("domain", new HashMap<String, String>());
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
