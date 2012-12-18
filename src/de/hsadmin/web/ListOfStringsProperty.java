package de.hsadmin.web;

import java.util.List;

public class ListOfStringsProperty extends AbstractProperty {
	public List<String> properties; 
	
	public ListOfStringsProperty(){
// ??		this.properties = new List<String>();
	}
	
	public boolean Add(String string){
		 return properties.add(string);
	}
}
