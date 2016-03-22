package de.hsadmin.web;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Window;

import de.hsadmin.rpc.HSAdminSession;

public class SubWindowFactory extends AbstractWindowFactory {

	@Override
	public Window getSubWindow(HSTab parent, String type, String action, HSAdminSession session) 
	{
		if (type == null) {
			return null;
		}
		if (action.equalsIgnoreCase("help")) {
			return new HelpWindow();
		}
		final Map<String, String> whereContext = new HashMap<>();
		whereContext.put(parent.getSelectPropertyName(), parent.getSelectPropertyValue().toString());
		return new GenericFormWindow(parent, type, action, session, whereContext);
	}

}
