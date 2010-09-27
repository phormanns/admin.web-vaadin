package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="domain")
@SessionScoped
public class Domain {

	@ManagedProperty(value="#{context}")
	private Context context;
	
	@ManagedProperty(value="#{remote}")
	private Remote remote;

	@ManagedProperty(value="#{texts}")
	private Texts texts;

	private List<Map<String, String>> list = null;
	private String error = null;
	public Map<String, String> labels = null;
	public Map<String, String> urls = null;
	
	public String getError() {
		return error;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setRemote(Remote remote) {
		this.remote = remote;
	}

	public void setTexts(Texts texts) {
		this.texts = texts;
	}

	public List<Map<String, String>> getList() {
		if (list == null) {
			list = new ArrayList<Map<String,String>>();
			try {
				Map<String, String> whereParams = new HashMap<String, String>();
				Object testList = remote.callSearch("domain", context.getUser(), whereParams);
				if (testList != null && testList instanceof Object[]) {
					Object[] lst = (Object[])testList;
					for (int i = 0; i<lst.length; i++) {
						Object testRow = lst[i];
						if (testRow instanceof Map<?, ?>) {
							Map<?, ?> row = (Map<?, ?>) testRow;
							Map<String, String> dom = new HashMap<String, String>();
							for (String key : new String[] { "id", "name", "user", "hive", "pac", "since" }) {
								dom.put(key, (String) row.get(key));
							}
							list.add(dom);
						}
					}
				}
			} catch (HsarwebException e) {
				error = e.getMessage();
			}
		}
		return list;
	}

	public Map<String, String> getLabels() {
		if (labels == null) {
			labels = new HashMap<String, String>();
			for (String key : new String[]{ "name", "user" }) {
				labels.put(key, texts.getLabel("domain." + key + ".label"));
			}
			
		}
		return labels;
	}

	public Map<String, String> getUrls() {
		if (urls == null) {
			String path = context.getContextPath() + "/";
			urls = new HashMap<String, String>();
			for (String key : new String[]{ "edit", "delete" }) {
				urls.put(key, path + "domain/" + key + ".html");
			}
			
		}
		return urls;
	}

	public String delete() {
		System.out.println("domain.delete" + FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dom_id"));
		return null;
	}

	public String edit() {
		System.out.println("domain.edit" + FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dom_id"));
		return null;
	}
}
