package de.hsadmin.web;

import java.util.Map;

import de.hsadmin.web.config.ModuleConfig;

public interface Module {

	public abstract MainApplication getApplication();

	public abstract ModuleConfig getModuleConfig();

	public abstract void reload() throws HsarwebException;

	public abstract void setApplication(MainApplication mainApplication) throws HsarwebException;

	public abstract Object getComponent();

	public abstract Map<String,Map<String,Object>> getModuleProps();

}