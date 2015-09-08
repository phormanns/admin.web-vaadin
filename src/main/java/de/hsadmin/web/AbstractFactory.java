package de.hsadmin.web;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public abstract class AbstractFactory {

	public abstract IHSPanel getPanel(String panelType, HSAdminSession mainWindow, Object itemId) throws RpcException;
	
	public abstract IHSWindow getSubWindow(String type, String action, HSAdminSession session);

}
