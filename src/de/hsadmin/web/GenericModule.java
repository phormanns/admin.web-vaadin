package de.hsadmin.web;

import java.util.ArrayList;
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

	public List<String> getUsers() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Object callSearch = getApplication().getRemote().callSearch("user", new HashMap<String, String>());
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					if (row instanceof Map<?, ?>) {
						Object object = ((Map<?, ?>) row).get("name");
						if (object instanceof String) {
							list.add((String) object);
						}
					}
				}
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
			getApplication().showSystemException(e);
		}
		return list;
	}

	public List<String> getEMailAliases() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Object callSearch = getApplication().getRemote().callSearch("emailalias", new HashMap<String, String>());
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					if (row instanceof Map<?, ?>) {
						Object object = ((Map<?, ?>) row).get("name");
						if (object instanceof String) {
							list.add((String) object);
						}
					}
				}
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
			getApplication().showSystemException(e);
		}
		return list;
	}

	public List<String> getDomains() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Object callSearch = getApplication().getRemote().callSearch("domain", new HashMap<String, String>());
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					if (row instanceof Map<?, ?>) {
						Object object = ((Map<?, ?>) row).get("name");
						if (object instanceof String) {
							list.add((String) object);
						}
					}
				}
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
			getApplication().showSystemException(e);
		}
		return list;
	}

	public List<String> getPackets() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Object callSearch = getApplication().getRemote().callSearch("pac", new HashMap<String, String>());
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					if (row instanceof Map<?, ?>) {
						Object object = ((Map<?, ?>) row).get("name");
						if (object instanceof String) {
							list.add((String) object);
						}
					}
				}
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
			getApplication().showSystemException(e);
		}
		return list;
	}
	
}
