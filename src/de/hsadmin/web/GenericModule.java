package de.hsadmin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyTableColumn;

public abstract class GenericModule extends AbstractModule implements InsertAble, UpdateAble, DeleteAble {

	private static final long serialVersionUID = 1L;

	public void insertRow(Map<String, String> paramHash) throws HsarwebException {
		getApplication().getRemote().callAdd(getModuleConfig().getName(), paramHash);
	}

	public void deleteRow(Map<String, String> paramHash) throws HsarwebException {
		getApplication().getRemote().callDelete(getModuleConfig().getName(), paramHash);
	}
	
	public void updateRow(Map<String, String> paramHash) throws HsarwebException {
		Map<String, String> whereHash = new HashMap<String, String>();
		String idKey = findIdKey();
		whereHash.put(idKey, paramHash.get(idKey));
		getApplication().getRemote().callUpdate(getModuleConfig().getName(), paramHash, whereHash);
	}

	private String findIdKey() {
		List<PropertyConfig> propertyList = getModuleConfig().getPropertyList();
		String idKey = null;
		for (PropertyConfig propConf : propertyList) {
			PropertyTableColumn propTableColumn = propConf.getPropTableColumn();
			if (PropertyTableColumn.INTERNAL_KEY == propTableColumn) {
				idKey = propConf.getId();
				return idKey;
			}
		}
		return idKey;
	}
	
}
