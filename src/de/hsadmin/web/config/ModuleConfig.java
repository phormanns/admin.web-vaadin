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
	private List<PropertyConfig> propertyList;
	private Map<String, PropertyConfig> propertyMap;
	private LocaleConfig localeConfig;
	private boolean deleteAble;
	private boolean updateAble;
	private boolean addAble;
	private boolean searchAble;

	public ModuleConfig(String name) {
		this.name = name;
		propertyList = new ArrayList<PropertyConfig>();
		propertyMap = new HashMap<String, PropertyConfig>();
		localeConfig = new LocaleConfig(Locale.getDefault(), name);
		addAble = true;
		updateAble = true;
		deleteAble = true;
		searchAble = true;
	}
	
	public String getName() {
		return name;
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

	public boolean isDeleteAble() {
		return deleteAble;
	}

	public boolean isUpdateAble() {
		return updateAble;
	}

	public boolean isAddAble() {
		return addAble;
	}

	public boolean isSearchAble() {
		return searchAble;
	}

	public void setDeleteAble(boolean deleteAble) {
		this.deleteAble = deleteAble;
	}

	public void setUpdateAble(boolean updateAble) {
		this.updateAble = updateAble;
	}

	public void setAddAble(boolean addAble) {
		this.addAble = addAble;
	}
	
	public void setSearchAble(boolean searchAble) {
		this.searchAble = searchAble;
	}

	public int getNumOfColumns() {
		int numOfCols = 0;
		for (PropertyConfig prop : propertyList) {
			if (prop.getPropTableColumn() != PropertyTableColumn.NONE) {
				numOfCols++;
			}
		}
		if (isUpdateAble()) {
			numOfCols++;
		}
		if (isDeleteAble()) {
			numOfCols++;
		}
		return numOfCols;
	}
	
}
