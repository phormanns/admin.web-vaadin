package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.ModuleInfo;
import de.hsadmin.rpc.ModulesManager;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.RpcException;
import de.hsadmin.rpc.enums.DisplayPolicy;

public class HSTab extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;
	private final String module;
	private final Object itemId;
	private final String idPropertyName;
	
	private HorizontalLayout panelToolbar;
	private Table grid;


	public HSTab(String source, HSAdminSession session, String idPropertyName, Object itemId) {
		super();
		this.module = source;
		this.session = session;
		this.idPropertyName = idPropertyName;
		this.itemId = itemId;
		panelToolbar = new PanelToolbar(source);
		addComponent(panelToolbar);
		setComponentAlignment(panelToolbar, Alignment.MIDDLE_RIGHT);
		addComponent(getGrid(session.getModulesManager().module(source)));
	}

	private Table getGrid(ModuleInfo moduleInfo) 
	{
		grid = new Table();
		final Iterator<PropertyInfo> properties = moduleInfo.properties();
		while (properties.hasNext()) {
			final PropertyInfo propertyInfo = properties.next();
			if (DisplayPolicy.ALWAYS.equals(propertyInfo.getDisplayVisible())) {
				grid.addContainerProperty(propertyInfo.getModule() + "." + propertyInfo.getName(), String.class, "");
			}
		}
		grid.setPageLength(grid.size());
		grid.setSelectable(true);
		grid.setImmediate(true);
		grid.setSizeFull();
		return grid;
	}
	
	public void fillTable() throws RpcException 
	{
		final ModulesManager modulesManager = session.getModulesManager();
		final String grantingTicket = session.getGrantingTicket();
		final TicketService ticketService = session.getTicketService();
		final String serviceTicket = ticketService.getServiceTicket(grantingTicket);
		final String user = session.getUser();
		final HashMap<String, String> whereParams = new HashMap<String, String>();
		whereParams.put(idPropertyName, itemId.toString());
		try {
			final List<Map<String, Object>> emailaddressList = modulesManager.proxy(module).search(user, serviceTicket, whereParams);
			
			for (Map<String, Object> emailaddressHash : emailaddressList) {

				final Iterator<PropertyInfo> properties = session.getModulesManager().module(module).properties();
				final List<String> itemsList = new ArrayList<String>();
				while (properties.hasNext()) {
					final PropertyInfo propertyInfo = properties.next();
					if (DisplayPolicy.ALWAYS.equals(propertyInfo.getDisplayVisible())) {
						final Object value = emailaddressHash.get(propertyInfo.getName());
						itemsList.add(value == null ? "" : value.toString());
					}
				}

//				final String emailaddress = emailaddressHash.get("emailaddress").toString();
//				final Object[] targets = (Object[]) emailaddressHash.get("target");
//				final StringBuffer targetsBuff = new StringBuffer();
//				if (targets.length > 0) {
//					targetsBuff.append(targets[0]);
//				}
//				for (int idx = 1; idx < targets.length; idx++) {
//					targetsBuff.append(", ");
//					targetsBuff.append(targets[idx]);
//				}
//				final String target = targetsBuff.toString();
				
				grid.addItem(itemsList.toArray(), emailaddressHash.get("id"));
			}
		} catch (UnsupportedOperationException | XmlRpcException e) {
			throw new RpcException(e);
		}
		
	}

}
