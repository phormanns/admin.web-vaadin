package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.ModulesManager;
import de.hsadmin.rpc.ModulesManagerFactory;
import de.hsadmin.rpc.RpcException;

public class MainWindow extends CustomComponent implements HSAdminSession {

	private static final long serialVersionUID = 1L;
	
	public static final String[] SERVICE_URLS = new String[] { 
		"https://config2.hostsharing.net:443/hsar/xmlrpc/hsadmin", 
		"https://config.hostsharing.net:443/hsar/xmlrpc/hsadmin" 
	};

	private ModulesManager modulesManager;
	private TicketService ticketService;
	private String grantingTicket;
	private String username;
	private AbstractSplitPanel content;
	
	public MainWindow(TicketService ticketService, String grantingTicket, String username) {
		this.ticketService = ticketService;
		this.grantingTicket = grantingTicket;
		this.username = username;
		
		setSizeFull();
		Panel mainPanel = new Panel();
		mainPanel.setSizeFull();
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		setCompositionRoot(mainPanel);
		mainPanel.setContent(vl);
		
		try {
			final ModulesManagerFactory modulesManagerFactory = new ModulesManagerFactory(grantingTicket, username);
			modulesManager = modulesManagerFactory.newModulesManager(SERVICE_URLS);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		content = new HorizontalSplitPanel();
		content.setSizeFull();
		vl.addComponent(content);

		final EntryPointsSelector entryPoints = new EntryPointsSelector(this);
		entryPoints.setSizeFull();
		content.setFirstComponent(entryPoints);
		content.setSecondComponent(new MainPanel());
		content.setSplitPosition(26.6f);
	}

	/**
	 * Update center panel.
	 * @param source module name
	 * @param itemId 
	 */
	public void setCenterPanel(String source, Object itemId) {
		final AbstractPanelFactory panelFactory = FactoryProducer.getPanelFactory("panel");
		try {
			IHSPanel panel = panelFactory.getPanel(source, this, itemId);
			panel.setSizeFull();
			content.setSecondComponent(panel);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object[]> list(final String moduleName, String... columnNames) {
		final List<Object[]> resultList = new ArrayList<Object[]>();
		try {
			final List<Map<String, Object>> searchResult = modulesManager.proxy(moduleName).search(username, ticketService.getServiceTicket(grantingTicket), new HashMap<String, String>());
			for (Map<String, Object> valueMap : searchResult) {
				final Object[] valueArr = new Object[columnNames.length];
				for (int idx = 0; idx < columnNames.length; idx++) {
					valueArr[idx] = valueMap.get(columnNames[idx]);
				}
				resultList.add(valueArr);
			}
		} catch (XmlRpcException | RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}
	
	public String[] entryPointColumns(final String moduleName) {
		return modulesManager.entryPointColumns(moduleName);
	}

	@Override
	public String getGrantingTicket() {
		return grantingTicket;
	}

	@Override
	public TicketService getTicketService() {
		return ticketService;
	}

	@Override
	public ModulesManager getModulesManager() {
		return modulesManager;
	}

	@Override
	public String getUser() {
		return username;
	}
}
