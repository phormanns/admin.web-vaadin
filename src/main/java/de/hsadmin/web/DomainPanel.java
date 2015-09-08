package de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class DomainPanel extends CustomComponent implements IHSPanel {

	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;

	public DomainPanel(HSAdminSession session, Object itemId) {
		this.session = session;
		final Panel panel = new Panel();

		TabSheet tabsheet = createTabs(itemId);
		panel.setContent(tabsheet);

		setCompositionRoot(panel);
	}

	@Override
	public TabSheet createTabs(Object itemId) {

		TabSheet tabsheet = new TabSheet();
		HSTab emailTab = new HSTab("emailaddress", session, "domain", itemId);
		try {
			emailTab.fillTable();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tabsheet.addTab(emailTab, "emailaddress");

		return tabsheet;
	}

}