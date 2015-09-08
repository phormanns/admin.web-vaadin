package de.hsadmin.web;

import de.hsadmin.rpc.HSAdminSession;

public class SubWindowFactory extends AbstractFactory {

	@Override
	public IHSWindow getSubWindow(String type, String action) {

		if (type == null)
			return null;

		if (action.equalsIgnoreCase("help")) {
			return new HelpWindow();
		}

		if (type.equalsIgnoreCase("USER")) {
			if (action.equalsIgnoreCase("new"))
				return new NewUserWindow();
		}

		if (type.equalsIgnoreCase("PACKAGE")) {
			if (action.equalsIgnoreCase("new")) {
				return new NewPackageWindow();
			}
		}

		/*
		 * if(type.equalsIgnoreCase("PACKAGE")) return new PackagePanel();
		 */

		return null;
	}

	@Override
	public IHSPanel getPanel(String panelType, HSAdminSession session, Object itemId) {
		return null;
	}

}
