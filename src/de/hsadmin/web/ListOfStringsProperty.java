package de.hsadmin.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListOfStringsProperty implements XmlrpcProperty {
	
	public final List<String> properties; 
	
	public ListOfStringsProperty(){
		this.properties = new ArrayList<String>();
	}
	
	public boolean add(String string){
		 return properties.add(string);
	}

	@Override
	public Object toXmlrpcParam() {
		String[] result = new String[properties.size()];
		int idx = 0;
		for (String prop : properties) {
			result[idx] = prop;
			idx++;
		}
		return result;
	}

	public Iterator<String> stringsIterator() {
		return properties.iterator();
	}
}
