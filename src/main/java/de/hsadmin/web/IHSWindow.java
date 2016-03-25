package de.hsadmin.web;

import java.util.Map;

import com.vaadin.server.ErrorMessage;

public interface IHSWindow {
	
	public void setFormData(Map<String, Object> value, Map<String, String> selector);
	
	public Map<String, Object> getFormData();
	
	public Map<String, String> getUniqueWhereSelector();

	public void reload();
	
	public boolean isValid();

	public void setComponentError(ErrorMessage componentError);

}
