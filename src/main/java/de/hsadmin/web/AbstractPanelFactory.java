package de.hsadmin.web;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public abstract class AbstractPanelFactory {

	public abstract IHSPanel getPanel(String panelType, HSAdminSession mainWindow, Object itemId) throws RpcException;
	
}
