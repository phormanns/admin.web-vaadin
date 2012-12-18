package de.hsadmin.web;

import java.util.Map;

public interface DeleteAble {

	public void deleteRow(Map<String, AbstractProperty> paramHash) throws HsarwebException;
	
}
