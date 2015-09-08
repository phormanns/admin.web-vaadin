package de.hsadmin.web;

import de.hsadmin.rpc.HSAdminSession;

public abstract class AbstractFactory {

	public abstract IHSPanel getPanel(String panelType, HSAdminSession mainWindow, Object itemId);
	
	public abstract IHSWindow getSubWindow(String type, String action);

}
