package de.hsadmin.web;

import java.util.Map;

public interface UpdateAble {

	public void updateRow(Map<String, String> paramHash) throws HsarwebException;
	
}
