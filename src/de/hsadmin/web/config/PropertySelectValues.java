package de.hsadmin.web.config;

import java.util.Map;

public interface PropertySelectValues {

	public abstract Map<String, String> getSelectValues();
	
	public abstract boolean newItemsAllowed();
	
	public abstract boolean hasSelectList();

}
