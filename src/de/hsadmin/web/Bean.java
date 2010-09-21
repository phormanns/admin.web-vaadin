package de.hsadmin.web;

import java.text.DateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean 
@SessionScoped
public class Bean {

	public final static DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT); 
	
	public String getMessage() {
		return "Hallo Welt";
	}
	
	public String getTime() {
		return df.format(new Date());
	}
	
}
