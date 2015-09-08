package de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.HSAdminSession;

public class PackagePanel extends CustomComponent implements IHSPanel {

	private static final long serialVersionUID = 5397852733862398775L;
	
	private final HSAdminSession session;

	public PackagePanel(HSAdminSession session, Object itemId) {
		this.session = session;
		final Panel panel = new Panel();

		TabSheet tabsheet = createTabs(itemId);
		panel.setContent(tabsheet);

		setCompositionRoot(panel);
		setSizeFull();
	}

	@Override
	public TabSheet createTabs(Object itemId) {

		TabSheet tabsheet = new TabSheet();
		tabsheet.addTab(new HSTab("user", session, "pac", itemId), "User");
		tabsheet.addTab(new HSTab("database", session, "pac", itemId), "Data Base");
		tabsheet.addTab(new HSTab("alias", session, "pac", itemId), "Aliases");

		return tabsheet;
	}


}
