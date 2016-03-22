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
		final TabSheet tabsheet = new TabSheet();
		tabsheet.addSelectedTabChangeListener(this);
		tabsheet.addTab(new GenericForm("pac", session, itemId, "name"), I18N.getText("pac"));
		final HSTab usersTab = new HSTab("user", session, "pac", itemId, "name");
		usersTab.fillTable();
		tabsheet.addTab(usersTab, I18N.getText("user"));
		final HSTab aliasTab = new HSTab("emailalias", session, "pac", itemId, "name");
		tabsheet.addTab(aliasTab, I18N.getText("emailalias"));
		final HSTab domainTab = new HSTab("domain", session, "pac", itemId, "name");
		tabsheet.addTab(domainTab, I18N.getText("domain"));

		final HSTab mysqluserTab = new HSTab("mysqluser", session, "pac", itemId, "name");
		tabsheet.addTab(mysqluserTab, I18N.getText("mysqluser"));
		final HSTab mysqldbTab = new HSTab("mysqldb", session, "pac", itemId, "name");
		tabsheet.addTab(mysqldbTab, I18N.getText("mysqldb"));
		final HSTab postgresqluserTab = new HSTab("postgresqluser", session, "pac", itemId, "name");
		tabsheet.addTab(postgresqluserTab, I18N.getText("postgresqluser"));
		final HSTab postgresqldbTab = new HSTab("postgresqldb", session, "pac", itemId, "name");
		tabsheet.addTab(postgresqldbTab, I18N.getText("postgresqldb"));
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
