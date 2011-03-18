package de.hsadmin.web.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PropertyConfig implements Serializable, PropertyDefaultValue, PropertySelectValues {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;
	private String id;
	private Class<?> type;
	private PropertyTableColumn propTableColumn;
	private PropertyFormField propFormField;
	private PropertyDefaultValue defaultValue;
	private PropertySelectValues selectValues;
	
	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = PropertyTableColumn.DISPLAY;
		this.propFormField = PropertyFormField.READWRITE;
		this.defaultValue = null;
		this.selectValues = null;
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, PropertyFormField formField) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = PropertyTableColumn.DISPLAY;
		this.propFormField = formField;
		this.defaultValue = null;
		this.selectValues = null;
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, PropertyTableColumn tablecolumn) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = tablecolumn;
		this.propFormField = PropertyFormField.READWRITE;
		this.defaultValue = null;
		this.selectValues = null;
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, PropertyTableColumn tablecolumn, PropertyFormField formField) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = tablecolumn;
		this.propFormField = formField;
		this.defaultValue = null;
		this.selectValues = null;
	}

	public String getId() {
		return id;
	}
	
	public String getLabel() {
		return moduleConfig.getLabel(id);
	}

	public Class<?> getType() {
		return type;
	}

	public PropertyTableColumn getPropTableColumn() {
		return propTableColumn;
	}

	public PropertyFormField getPropFormField() {
		return propFormField;
	}

	public void setDefaultValue(PropertyDefaultValue defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setSelectValues(PropertySelectValues selectValues) {
		this.selectValues = selectValues;
	}

	public String getDefaultValue() {
		if (defaultValue != null) {
			return defaultValue.getDefaultValue();
		}
		return "";
	}

	public Map<String, String> getSelectValues() {
		if (selectValues != null) {
			return selectValues.getSelectValues();
		}
		return new HashMap<String, String>();
	}
	
	public boolean newItemsAllowed() {
		if (selectValues != null) {
			return selectValues.newItemsAllowed();
		}
		return propFormField == PropertyFormField.READWRITE || propFormField == PropertyFormField.WRITEONCE;
	}

	@Override
	public boolean hasSelectList() {
		if (selectValues != null) {
			return selectValues.hasSelectList();
		}
		return false;
	}
	
}
