package de.hsadmin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;

import de.hsadmin.model.IRemote;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.RpcException;

public class EntryPointsSelector extends CustomComponent implements ItemClickListener, SelectedTabChangeListener {

	private static final long serialVersionUID = 1L;
	private final MainWindow mainWindow;
	
	private Accordion accordion;

	public EntryPointsSelector(final MainWindow mainWindow) throws RpcException {
		this.mainWindow = mainWindow;
		final Panel panel = new Panel();
		accordion = new Accordion();
		accordion.setSizeFull();
		createTabs();
		panel.setContent(accordion);
		panel.setSizeFull();
		setCompositionRoot(panel);
		accordion.addSelectedTabChangeListener(this);
	}

	private void createTabs() throws RpcException {
		final String role = getRole();
		final AbstractEntryPointsFactory entryPointsFactory = FactoryProducer.getEntryPointsFactory("default");
		boolean hasFirstTab = false;
		for(String tabName : entryPointsFactory.getEntryPointNames(role)) {
			accordion.addTab(new EntryPoint(this, tabName), I18N.getText(tabName));
			hasFirstTab = true;
		}
		if (hasFirstTab) {
			final Component component = accordion.getTab(0).getComponent();
			if (component instanceof EntryPoint) {
				((EntryPoint) component).fillTable();
			}
		}
	}

	public String getRole() throws RpcException {
		final IRemote rolesProxy = mainWindow.getModulesManager().proxy("role");
		final String user = mainWindow.getUser();
		final TicketService ticketService = mainWindow.getTicketService();
		final String grantingTicket = mainWindow.getGrantingTicket();
		final String serviceTicket = ticketService.getServiceTicket(grantingTicket);
		try {
			final List<Map<String,Object>> list = rolesProxy.search(user, serviceTicket, new HashMap<String, String>());
			return (String) list.get(0).get("role");
		} catch (XmlRpcException e) {
			throw new RpcException(e);
		}
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		Table table = (Table) event.getSource();
		mainWindow.setCenterPanel((String) table.getData(), event.getItemId());
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		final Component selectedTab = event.getTabSheet().getSelectedTab();
		if (selectedTab instanceof EntryPoint) {
			((EntryPoint) selectedTab).fillTable();
		}
	}
	
	public MainWindow getMainWindow() {
		return mainWindow;
	}
}