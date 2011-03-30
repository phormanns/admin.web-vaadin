package de.hsadmin.web;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertySelectValues;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.EMailTargetPropertyFieldFactory;
import de.hsadmin.web.vaadin.SelectPropertyFieldFactory;

public class EMailAddressModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		MainApplication application = getApplication();
		moduleConfig = new ModuleConfig("emailaddress", application.getLocale());
		String login = application.getLogin();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
		PropertyConfig fullAddressProp = new PropertyConfig(moduleConfig, "emailaddress", String.class);
		fullAddressProp.setReadOnly(true);
		PropertyConfig localpartProp = new PropertyConfig(moduleConfig, "localpart", String.class, PropertyTableColumn.HIDDEN);
		localpartProp.setWriteOnce(true);
		PropertyConfig subdomainProp = new PropertyConfig(moduleConfig, "subdomain", String.class, PropertyTableColumn.HIDDEN);
		subdomainProp.setWriteOnce(true);
		PropertyConfig domainProp = new PropertyConfig(moduleConfig, "domain", String.class, PropertyTableColumn.HIDDEN, new SelectPropertyFieldFactory());
		domainProp.setSelectValues(new PropertySelectValues() {
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
		domainProp.setWriteOnce(true);
		PropertyConfig targetProp = new PropertyConfig(moduleConfig, "target", String.class, new EMailTargetPropertyFieldFactory(this));
		targetProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac + "-";
			}
		});
		PropertyConfig domAdminProp = new PropertyConfig(moduleConfig, "admin", String.class, PropertyTableColumn.HIDDEN);
		domAdminProp.setReadOnly(true);
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN);
		pacProp.setReadOnly(true);
		PropertyConfig fulldomainProp = new PropertyConfig(moduleConfig, "fulldomain", String.class, PropertyTableColumn.HIDDEN);
		fulldomainProp.setReadOnly(true);
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(fullAddressProp);
		moduleConfig.addProperty(localpartProp);
		moduleConfig.addProperty(subdomainProp);
		moduleConfig.addProperty(domainProp);
		moduleConfig.addProperty(targetProp);
		moduleConfig.addProperty(domAdminProp);
		moduleConfig.addProperty(pacProp);
		moduleConfig.addProperty(fulldomainProp);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
