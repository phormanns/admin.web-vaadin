package de.hsadmin.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class PackagePanel extends CustomComponent implements IHSPanel, SelectedTabChangeListener {

	private static final long serialVersionUID = 5397852733862398775L;
	
	private final HSAdminSession session;

	public PackagePanel(HSAdminSession session, Object itemId) throws RpcException {
		this.session = session;
		final Panel panel = new Panel();
		panel.setSizeFull();
		final TabSheet tabsheet = createTabs(itemId);
		tabsheet.setSizeFull();
		panel.setContent(tabsheet);
		setCompositionRoot(panel);
	}

	@Override
	public TabSheet createTabs(Object itemId) throws RpcException 
	{
		TabSheet tabsheet = new TabSheet();
		tabsheet.addSelectedTabChangeListener(this);
		HSTab usersTab = new HSTab("user", session, "pac", itemId, "name");
		usersTab.fillTable();
		tabsheet.addTab(usersTab, "user");
		HSTab aliasTab = new HSTab("emailalias", session, "pac", itemId, "name");
		tabsheet.addTab(aliasTab, "emailalias");

		HSTab domainTab = new HSTab("domain", session, "pac", itemId, "name");
		tabsheet.addTab(domainTab, "domain");

		HSTab mysqluserTab = new HSTab("mysqluser", session, "pac", itemId, "name");
		tabsheet.addTab(mysqluserTab, "mysqluser");
		HSTab mysqldbTab = new HSTab("mysqldb", session, "pac", itemId, "name");
		tabsheet.addTab(mysqldbTab, "mysqldb");
		HSTab postgresqluserTab = new HSTab("postgresqluser", session, "pac", itemId, "name");
		tabsheet.addTab(postgresqluserTab, "postgresqluser");
		HSTab postgresqldbTab = new HSTab("postgresqldb", session, "pac", itemId, "name");
		tabsheet.addTab(postgresqldbTab, "postgresqldb");
		
		return tabsheet;
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		final Component tab = event.getTabSheet().getSelectedTab();
		if (tab instanceof HSTab) {
			((HSTab) tab).fillTable();
		}
	}

}
