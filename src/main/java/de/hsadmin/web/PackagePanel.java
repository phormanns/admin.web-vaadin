package de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class PackagePanel extends CustomComponent implements IHSPanel {

	private static final long serialVersionUID = 5397852733862398775L;
	
	private final HSAdminSession session;

	public PackagePanel(HSAdminSession session, Object itemId) throws RpcException {
		this.session = session;
		final Panel panel = new Panel();
		final TabSheet tabsheet = createTabs(itemId);
		tabsheet.setSizeFull();
		panel.setContent(tabsheet);
		setCompositionRoot(panel);
	}

	@Override
	public TabSheet createTabs(Object itemId) throws RpcException 
	{
		TabSheet tabsheet = new TabSheet();
		HSTab usersTab = new HSTab("user", session, "pac", itemId);
		usersTab.fillTable();
		tabsheet.addTab(usersTab, "user");
		HSTab aliasTab = new HSTab("emailalias", session, "pac", itemId);
		aliasTab.fillTable();
		tabsheet.addTab(aliasTab, "emailalias");
		return tabsheet;
	}

}
