package de.hsadmin.web;

import java.util.Map;

public interface DeleteAble {

	public void deleteRow(Map<String, XmlrpcProperty> paramHash) throws HsarwebException;
	
}
