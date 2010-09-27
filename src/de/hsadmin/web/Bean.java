package de.hsadmin.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="hello")
@SessionScoped
public class Bean {

	public final static DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT); 
	
	@ManagedProperty(value="#{context}")
	private Context context;
	
	@ManagedProperty(value="#{remote}")
	private Remote remote;
	
	public String getMessage() {
		Map<String, String> where = new HashMap<String, String>();
		where.put("name", context.getUser());
		String name = "Welt";
		try {
			Object test = remote.callSearch("user", context.getUser(), where);
			if (test instanceof Object[] && ((Object[])test).length > 0 ) {
				if (((Object[])test)[0] instanceof Map) {
					name = (String) ((Map<?, ?>)((Object[])test)[0]).get("comment"); 
				}
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
		}
		return "Hallo " + name;
	}
	
	public String getTime() {
		return df.format(new Date());
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	public void setRemote(Remote remote) {
		this.remote = remote;
	}

}
