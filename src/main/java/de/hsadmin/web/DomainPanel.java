package de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.RpcException;

public class DomainPanel extends CustomComponent implements IHSPanel{

	private static final long serialVersionUID = 2223638269308264340L;

	public DomainPanel() {

		final Panel panel  = new Panel();
		
		TabSheet tabsheet = createTabs();
        panel.setContent(tabsheet);

		setCompositionRoot(panel);
	}

	@Override
	public TabSheet createTabs() {

		TabSheet tabsheet = new TabSheet();
		HSTab emailTab = new HSTab("email");
		try {
			emailTab.fillTable();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tabsheet.addTab(emailTab, "Email Address");

		return tabsheet;
	}
}