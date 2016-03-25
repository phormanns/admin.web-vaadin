package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
	
	private static final Logger LOG = Logger.getLogger(MainWindow.class);
	
	public static final String[] SERVICE_URLS = new String[] { 
		"https://config2.hostsharing.net:443/hsar/xmlrpc/hsadmin", 
		"https://config.hostsharing.net:443/hsar/xmlrpc/hsadmin" 
	};

	final private TicketService ticketService;
	final private String grantingTicket;
	final private String username;

	private ModulesManager modulesManager;
	private AbstractSplitPanel content;
	
	public MainWindow(final TicketService ticketService, final String grantingTicket, final String username) {
		this.ticketService = ticketService;
		this.grantingTicket = grantingTicket;
		this.username = username;
		
		setSizeFull();
		final Panel mainPanel = new Panel();
		mainPanel.setSizeFull();
		final VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		setCompositionRoot(mainPanel);
		mainPanel.setContent(vl);
		
		try {
			final MainToolbar mainToolbar = new MainToolbar(this);
			vl.addComponent(mainToolbar);
			vl.setExpandRatio(mainToolbar, 0.0f);
			final ModulesManagerFactory modulesManagerFactory = new ModulesManagerFactory(grantingTicket, username);
			modulesManager = modulesManagerFactory.newModulesManager(SERVICE_URLS);
			content = new HorizontalSplitPanel();
			content.setSizeFull();
			vl.addComponent(content);
			vl.setExpandRatio(content, 1.0f);
			final EntryPointsSelector entryPoints = new EntryPointsSelector(this);
			entryPoints.setSizeFull();
			content.setFirstComponent(entryPoints);
			content.setSecondComponent(new MainPanel());
			content.setSplitPosition(26.6f);
		} catch (RpcException e) {
			LOG.fatal(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Update center panel.
	 * @param source module name
	 * @param itemId 
	 */
	public void setCenterPanel(final String source, final Object itemId) {
		final AbstractPanelFactory panelFactory = FactoryProducer.getPanelFactory("panel");
		try {
			IHSPanel panel = panelFactory.getPanel(source, this, itemId);
			panel.setSizeFull();
			content.setSecondComponent(panel);
		} catch (RpcException e) {
			LOG.fatal(e.getLocalizedMessage(), e);
		}
	}

	public List<Object[]> list(final String moduleName, final String... columnNames) {
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
			LOG.fatal(e.getLocalizedMessage(), e);
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
