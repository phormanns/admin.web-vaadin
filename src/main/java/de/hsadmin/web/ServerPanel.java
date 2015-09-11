package de.hsadmin.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import de.hsadmin.rpc.HSAdminSession;

public class ServerPanel extends CustomComponent implements IHSPanel, SelectedTabChangeListener {

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
		tabsheet.addTab(new HSTab("package", session, "hive", itemId, "name"), "Packages");
		tabsheet.addTab(new HSTab("network", session, "hive", itemId, "name"), "Network");
		
		return tabsheet;
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		Component tab = event.getTabSheet().getSelectedTab();
		if (tab instanceof HSTab) {
			((HSTab) tab).fillTable();
		}
	}

}
