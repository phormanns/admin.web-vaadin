package de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class DomainPanel extends CustomComponent implements IHSPanel {

	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;

	public DomainPanel(HSAdminSession session, Object itemId) throws RpcException {
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
		HSTab emailTab = new HSTab("emailaddress", session, "domain", itemId);
		emailTab.fillTable();
		tabsheet.addTab(emailTab, "emailaddress");
		return tabsheet;
	}

}