package de.hsadmin.web.config;

import de.hsadmin.web.HsarwebException;

public interface ComponentFactory {

	public Object initComponent() throws HsarwebException;
	
	public void loadData() throws HsarwebException;
	
}
