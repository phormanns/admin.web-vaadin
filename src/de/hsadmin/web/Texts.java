package de.hsadmin.web;

import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="texts")
@SessionScoped
public class Texts {

	public String getLabel(String key) {
		ResourceBundle resource = ResourceBundle.getBundle("texts.messages");
		return resource.getString(key);
	}

}
