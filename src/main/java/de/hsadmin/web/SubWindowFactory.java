package de.hsadmin.web;

import de.hsadmin.rpc.HSAdminSession;

public class SubWindowFactory extends AbstractFactory {

	@Override
	public IHSWindow getSubWindow(String type, String action, HSAdminSession session) {

		if (type == null) {
			return null;
		}
		if (action.equalsIgnoreCase("help")) {
			return new HelpWindow();
		}
		return new GenericFormWindow(type, action, session);
	}

	@Override
	public IHSPanel getPanel(String panelType, HSAdminSession session, Object itemId) {
		return null;
	}

}
