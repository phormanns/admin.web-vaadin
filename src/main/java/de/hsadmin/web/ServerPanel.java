package de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.HSAdminSession;

public class ServerPanel extends CustomComponent implements IHSPanel {

	private static final long serialVersionUID = 5720017114380927259L;
	
	private final HSAdminSession session;
	
	public ServerPanel(HSAdminSession session, Object itemId) {
		this.session = session;
		final Panel panel  = new Panel();

		TabSheet tabsheet = createTabs(itemId);
		tabsheet.setSizeFull();
        panel.setContent(tabsheet);

		setCompositionRoot(panel);
		setSizeFull();
	}

	@Override
	public TabSheet createTabs(Object itemId) {

		TabSheet tabsheet = new TabSheet();
		tabsheet.addTab(new HSTab("package", session, "hive", itemId), "Packages");
		tabsheet.addTab(new HSTab("network", session, "hive", itemId), "Network");
		
		return tabsheet;
	}

}
