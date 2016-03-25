package de.hsadmin.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class DomainPanel extends CustomComponent implements IHSPanel, SelectedTabChangeListener {

	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;

	public DomainPanel(HSAdminSession session, Object itemId) throws RpcException {
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
		tabsheet.addTab(new GenericForm("domain", session, itemId, "name"), I18N.getText("domain"));
		HSTab emailTab = new HSTab("emailaddress", session, "domain", itemId, "id");
		emailTab.fillTable();
		tabsheet.addTab(emailTab, I18N.getText("emailaddress"));
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