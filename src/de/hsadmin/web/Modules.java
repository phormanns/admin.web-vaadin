package de.hsadmin.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean 
@ApplicationScoped
public class Modules {

	public String[] pageNames = new String[] { "hello" };
	
	public List<String> getPageNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (String name : pageNames) {
			names.add(name); 
		}
		return names;
	}
}
