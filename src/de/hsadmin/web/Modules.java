package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="modules") 
@SessionScoped
public class Modules {

	@ManagedProperty(value="#{context}")
	private Context context;
	
	@ManagedProperty(value="#{texts}")
	private Texts texts;

	public String[] pageNames = new String[] { "hello", "domain" };
	public Map<String, String> labels = null;
	public Map<String, String> urls = null;
	
	public List<String> getPageNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (String name : pageNames) {
			names.add(name); 
		}
		return names;
	}
	
	public Map<String, String> getLabels() {
		if (labels == null) {
			labels = new HashMap<String, String>();
			for (String key : pageNames) {
				labels.put(key, texts.getLabel(key + ".label"));
			}
		}
		return labels;
	}

	public Map<String, String> getUrls() {
		if (urls == null) {
			String path = context.getContextPath() + "/";
			urls = new HashMap<String, String>();
			for (String key : pageNames) {
				urls.put(key, path + key + "/index.html");
			}
		}
		return urls;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setTexts(Texts texts) {
		this.texts = texts;
	}

}
