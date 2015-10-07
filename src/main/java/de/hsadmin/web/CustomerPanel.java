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
		tabsheet.setSizeFull();
		tabsheet.addSelectedTabChangeListener(this);
		tabsheet.addSelectedTabChangeListener(this);
		tabsheet.addTab(new GenericForm("customer", session, itemId, "name"), I18N.getText("customer"));
		final HSTab usersTab = new HSTab("contact", session, "customer", itemId, "email");
		usersTab.fillTable();
		tabsheet.addTab(usersTab, I18N.getText("contact"));
		HSTab aliasTab = new HSTab("mandat", session, "customer", itemId, "mandatRef");
		tabsheet.addTab(aliasTab, I18N.getText("mandat"));
		HSTab pacTab = new HSTab("pac", session, "customer", itemId, "name");
		tabsheet.addTab(pacTab, I18N.getText("pac"));
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
