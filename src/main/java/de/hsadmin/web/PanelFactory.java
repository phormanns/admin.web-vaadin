package de.hsadmin.web;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class PanelFactory extends AbstractPanelFactory {

	@Override
	public IHSPanel getPanel(String panelType, HSAdminSession session, Object itemId) throws RpcException 
	{
		IHSPanel instance = null;
		if (panelType.equals("hive")) {
			instance = new ServerPanel(session, itemId);
		}
		if (panelType.equals("domain")) {
			instance = new DomainPanel(session, itemId);
		}
		if (panelType.equals("pac")) {
			instance = new PackagePanel(session, itemId);
		}
		if (panelType.equals("customer")) {
			instance = new CustomerPanel(session, itemId);
		}
		return instance;
	}

}
