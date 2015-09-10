package de.hsadmin.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class CustomerPanel extends CustomComponent implements IHSPanel, SelectedTabChangeListener {

	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;

	public CustomerPanel(HSAdminSession session, Object itemId) throws RpcException {
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
		tabsheet.addSelectedTabChangeListener(this);
		HSTab usersTab = new HSTab("contact", session, "customer", itemId);
		usersTab.fillTable();
		tabsheet.addTab(usersTab, "contact");
		HSTab aliasTab = new HSTab("mandat", session, "customer", itemId);
		tabsheet.addTab(aliasTab, "mandat");
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
