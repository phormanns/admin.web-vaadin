package de.hsadmin.web.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.hsadmin.web.vaadin.DefaultPropertyFieldFactory;

public class PropertyConfig implements Serializable, PropertyDefaultValue, PropertySelectValues, PropertyFormVisible {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;
	private String id;
	private Class<?> type;
	private PropertyTableColumn propTableColumn;
	private PropertyFieldFactory propFieldFactory;
	private PropertyDefaultValue defaultValue;
	private PropertySelectValues selectValues;
	private boolean showInForm = true;
	private float expandRatio;
	
	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = PropertyTableColumn.DISPLAY;
		this.setPropFieldFactory(new DefaultPropertyFieldFactory());
		this.defaultValue = null;
		this.selectValues = null;
		this.setExpandRatio(0.5f);
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, PropertyFieldFactory fieldFactory) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = PropertyTableColumn.DISPLAY;
		this.setPropFieldFactory(fieldFactory);
		this.defaultValue = null;
		this.selectValues = null;
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, PropertyTableColumn tablecolumn) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = tablecolumn;
		this.setPropFieldFactory(new DefaultPropertyFieldFactory());
		this.defaultValue = null;
		this.selectValues = null;
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, PropertyTableColumn tablecolumn, PropertyFieldFactory fieldFactory) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.propTableColumn = tablecolumn;
		this.setPropFieldFactory(fieldFactory);
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
		return true;
	}

	@Override
	public boolean hasSelectList() {
		if (selectValues != null) {
			return selectValues.hasSelectList();
		}
		return false;
	}

	public void setPropFieldFactory(PropertyFieldFactory propFieldFactory) {
		this.propFieldFactory = propFieldFactory;
	}

	public PropertyFieldFactory getPropFieldFactory() {
		return propFieldFactory;
	}

	public void setReadOnly(boolean readOnly) {
		propFieldFactory.setReadOnly(readOnly);
	}

	public void setWriteOnce(boolean writeOnce) {
		propFieldFactory.setWriteOnce(writeOnce);
	}

	@Override
	public boolean isShowInForm() {
		return showInForm;
	}

	@Override
	public void setShowInForm(boolean show) {
		showInForm = show;
	}

	public float getExpandRatio() {
		return expandRatio;
	}

	public void setExpandRatio(float expandRatio) {
		this.expandRatio = expandRatio;
	}
	
}
