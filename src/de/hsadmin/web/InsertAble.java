package de.hsadmin.web;

import java.util.Map;

public interface InsertAble {

	public void insertRow(Map<String, XmlrpcProperty> paramHash) throws HsarwebException;
	
}
