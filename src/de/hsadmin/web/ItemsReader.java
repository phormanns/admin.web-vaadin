package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsReader {
	
	public static List<Object>  readItemList(MainApplication app, String module, String property) throws HsarwebException {
		final List<Object> itemsList = new ArrayList<Object>();
		Object custListObj = app.getRemote().callSearch(module, new HashMap<String, AbstractProperty>());
		if (custListObj instanceof Object[]) {
			Object[] custList = (Object[]) custListObj;
			for (Object custObj : custList) {
				if (custObj instanceof Map<?, ?>) {
					Map<?, ?> custHash = (Map<?, ?>)custObj;
					itemsList.add(custHash.get(property));
				}
			}
		}
		return itemsList;
	}

}