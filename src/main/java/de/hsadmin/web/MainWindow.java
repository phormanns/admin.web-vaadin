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
	
	public static final String SERVICE_URL = "https://config-test.hostsharing.net:443/hsar/xmlrpc/hsadmin";

//	private static final String USERNAME = "hsh98";
//	private static final String PASSWORD = "Diego.R!";

	private static final String USERNAME = "ad";
	private static final String PASSWORD = "adA$M123";

	private ModulesManager modulesManager;
	private TicketService ticketService;
	private String grantingTicket;

	private AbstractSplitPanel content;
	
	
	
	public MainWindow() {
		ticketService = new TicketService();
		final String username = USERNAME;
		final String password = PASSWORD;
		try {
			grantingTicket = ticketService.getGrantingTicket(username, password);
			final ModulesManagerFactory modulesManagerFactory = new ModulesManagerFactory(grantingTicket, username);
			modulesManager = modulesManagerFactory.newModulesManager(SERVICE_URL);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setSizeFull();

		Panel mainPanel = new Panel();
		mainPanel.setSizeFull();
		VerticalLayout vl = new VerticalLayout();
		
		content = new HorizontalSplitPanel();
		content.setSizeFull();
		vl.addComponent(content);

		final EntryPointsSelector entryPoints = new EntryPointsSelector(this);
		entryPoints.setSizeFull();
		content.setFirstComponent(entryPoints);

		content.setSecondComponent(new MainPanel());

		content.setSplitPosition(30.0f);
		setCompositionRoot(mainPanel);
		mainPanel.setContent(vl);

	}

	/**
	 * Update center panel.
	 * @param source module name
	 * @param itemId 
	 */
	public void setCenterPanel(String source, Object itemId) {
		final AbstractFactory panelFactory = FactoryProducer.getFactory("panel");
		final IHSPanel panel = panelFactory.getPanel(source, this, itemId);
		content.setSecondComponent(panel);
	}

	public List<Object[]> list(final String moduleName, String... columnNames) {
		final List<Object[]> resultList = new ArrayList<Object[]>();
		try {
			final List<Map<String, Object>> searchResult = modulesManager.proxy(moduleName).search(USERNAME, ticketService.getServiceTicket(grantingTicket), new HashMap<String, String>());
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
		return USERNAME;
	}
}
