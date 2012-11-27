package de.hsadmin.web;

import java.util.Map;

public interface InsertAble {

	public void insertRow(Map<String, AbstractProperty> paramHash) throws HsarwebException;
	
}
