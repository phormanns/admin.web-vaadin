package de.hsadmin.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="modules") 
@SessionScoped
public class Modules {

	@ManagedProperty(value="#{context}")
	private Context context;
	
	public String[] pageNames = new String[] { "hello" };
	
	public List<String> getPageNames() {
		ArrayList<String> names = new ArrayList<String>();
		String path = context.getContextPath() + "/";
		names.add(path + context.getUser());
		for (String name : pageNames) {
			names.add(path + name); 
		}
		return names;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
