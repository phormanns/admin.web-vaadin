package de.hsadmin.web;

import de.hsadmin.web.config.ModuleConfig;

public interface Module {

	public abstract MainApplication getApplication();

	public abstract ModuleConfig getModuleConfig();

	public abstract void reload() throws HsarwebException;

	public abstract void setApplication(MainApplication mainApplication) throws HsarwebException;

	public abstract Object getComponent();

}