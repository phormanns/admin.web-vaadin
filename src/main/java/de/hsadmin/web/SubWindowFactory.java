package de.hsadmin.web;

import com.vaadin.ui.Window;

import de.hsadmin.rpc.HSAdminSession;

public class SubWindowFactory extends AbstractWindowFactory {

	@Override
	public Window getSubWindow(HSTab parent, String type, String action, HSAdminSession session) {

		if (type == null) {
			return null;
		}
		if (action.equalsIgnoreCase("help")) {
			return new HelpWindow();
		}
		return new GenericFormWindow(parent, type, action, session);
	}

}
