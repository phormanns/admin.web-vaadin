package de.hsadmin.web.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ModuleConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String remoteName;
	private List<PropertyConfig> propertyList;
	private Map<String, PropertyConfig> propertyMap;
	private LocaleConfig localeConfig;

	public ModuleConfig(String name, Locale locale) {
		this.name = name;
		this.remoteName = name;
		propertyList = new ArrayList<PropertyConfig>();
		propertyMap = new HashMap<String, PropertyConfig>();
		localeConfig = new LocaleConfig(locale, name);
	}
	
	public String getName() {
		return name;
	}

	public String getRemoteName() {
		return remoteName;
	}

	public void setRemoteName(String name) {
		remoteName = name;
	}

	public void addProperty(PropertyConfig property) {
		propertyList.add(property);
		propertyMap.put(property.getId(), property);
	}

	public List<PropertyConfig> getPropertyList() {
		return propertyList;
	}

	public PropertyConfig getProperty(String id) {
		return propertyMap.get(id);
	}

	public String getLabel(String key) {
		return localeConfig.getText(key);
	}

	public int getNumOfColumns() {
		int numOfCols = 0;
		for (PropertyConfig prop : propertyList) {
			if (prop.getPropTableColumn() != PropertyTableColumn.NONE) {
				numOfCols++;
			}
		}
		return numOfCols;
	}
	
}
