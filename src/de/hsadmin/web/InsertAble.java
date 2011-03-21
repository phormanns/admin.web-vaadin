package de.hsadmin.web;

import java.util.Map;

public interface InsertAble {

	public void insertRow(Map<String, String> paramHash) throws HsarwebException;
	
}
