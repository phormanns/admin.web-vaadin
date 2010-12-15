package de.hsadmin.web.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModuleConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private List<PropertyConfig> propertyList;
	private LocaleConfig localeConfig;

	public ModuleConfig(String name) {
		this.name = name;
		propertyList = new ArrayList<PropertyConfig>();
		localeConfig = new LocaleConfig(Locale.getDefault(), name);
	}
	
	public String getName() {
		return name;
	}

	public void addProperty(PropertyConfig property) {
		this.propertyList.add(property);
	}

	public List<PropertyConfig> getPropertyList() {
		return propertyList;
	}

	public String getLabel(String key) {
		return localeConfig.getText(key);
	}
	
}
