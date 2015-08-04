package de.hsadmin.rpc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ModuleInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	final private String name;
	final private Map<String, PropertyInfo> propertiesByName;
	final private SortedMap<Integer, PropertyInfo> propertiesBySequence;
	
	public ModuleInfo(final String name) {
		this.name = name;
		this.propertiesByName = new HashMap<String, PropertyInfo>();
		this.propertiesBySequence = new TreeMap<Integer, PropertyInfo>();
	}

	public String getName() {
		return name;
	}
	
	public PropertyInfo propertyInfo(final String name) {
		return propertiesByName.get(name);
	}
	
	public Iterator<PropertyInfo> properties() {
		return propertiesBySequence.values().iterator();
	}
	
	public void add(final PropertyInfo propInfo) {
		propertiesByName.put(propInfo.getName(), propInfo);
		propertiesBySequence.put(propInfo.getDisplaySequence(), propInfo);
	}
	
}
