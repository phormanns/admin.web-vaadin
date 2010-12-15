package de.hsadmin.web.config;

import java.io.Serializable;

public class PropertyConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;
	private String id;
	private Class<?> type;
	private String defaultValue;
	private boolean hidden;
	private boolean ident;
	
	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, String defaultValue) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.defaultValue = defaultValue;
		this.setHidden(false);
		this.setIdent(false);
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, String defaultValue, boolean hidden) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.defaultValue = defaultValue;
		this.setHidden(hidden);
		this.setIdent(false);
	}

	public PropertyConfig(ModuleConfig moduleConfig, String id, Class<?> clasz, String defaultValue, boolean hidden, boolean ident) {
		this.moduleConfig = moduleConfig;
		this.id = id;
		this.type = clasz;
		this.defaultValue = defaultValue;
		this.setHidden(hidden);
		this.setIdent(ident);
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return moduleConfig.getLabel(id);
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setIdent(boolean ident) {
		this.ident = ident;
	}

	public boolean isIdent() {
		return ident;
	}

}
