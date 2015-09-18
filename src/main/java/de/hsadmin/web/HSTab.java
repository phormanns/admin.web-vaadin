package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
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

public class HSTab extends CustomComponent {
	
	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;
	private final String module;
	private final Object selectPropertyValue;
	private final String selectPropertyName;
	private final String rowIdPropertyName;
	
	private HorizontalLayout panelToolbar;
	private Table grid;
	private ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages");


	public HSTab(String source, HSAdminSession session, String selectPropertyName, Object selectPropertyValue, String rowIdPropertyName) {
		super();
		setSizeFull();
		this.module = source;
		this.session = session;
		this.selectPropertyName = selectPropertyName;
		this.selectPropertyValue = selectPropertyValue;
		this.rowIdPropertyName = rowIdPropertyName;
		final Table dataTable = getGrid(session.getModulesManager().module(source));
		panelToolbar = new PanelToolbar(source, session, this);
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(panelToolbar);
		layout.setComponentAlignment(panelToolbar, Alignment.TOP_RIGHT);
		layout.addComponent(dataTable);
		layout.setExpandRatio(dataTable, 1.0f);
		dataTable.setHeight("100%");
		setCompositionRoot(layout);
	}

	private Table getGrid(ModuleInfo moduleInfo) 
	{
		grid = new Table();
		final Iterator<PropertyInfo> properties = moduleInfo.properties();
		while (properties.hasNext()) {
			final PropertyInfo propertyInfo = properties.next();
			if (DisplayPolicy.ALWAYS.equals(propertyInfo.getDisplayVisible())) {
				String columnHeader;
				try{
					columnHeader = resourceBundle.getString(propertyInfo.getModule() + "." + propertyInfo.getName());
				}catch(MissingResourceException e){
					columnHeader = propertyInfo.getModule() + "." + propertyInfo.getName();
				}
				grid.addContainerProperty(columnHeader, String.class, "");
			}
		}
//		grid.setPageLength(0);
		grid.setSelectable(true);
		grid.setImmediate(true);
		grid.setSizeFull();
		return grid;
	}
	
	public void fillTable() 
	{
		grid.removeAllItems();
		Object firstId = null;
		try {
			final ModulesManager modulesManager = session.getModulesManager();
			final String grantingTicket = session.getGrantingTicket();
			final TicketService ticketService = session.getTicketService();
			final String serviceTicket = ticketService.getServiceTicket(grantingTicket);
			final String user = session.getUser();
			final HashMap<String, String> whereParams = new HashMap<String, String>();
			whereParams.put(selectPropertyName, selectPropertyValue.toString());
			try {
				final List<Map<String, Object>> objectsList = modulesManager.proxy(module).search(user, serviceTicket, whereParams);
				
				for (Map<String, Object> objectHash : objectsList) {

					final Iterator<PropertyInfo> properties = session.getModulesManager().module(module).properties();
					final List<String> itemsList = new ArrayList<String>();
					while (properties.hasNext()) {
						final PropertyInfo propertyInfo = properties.next();
						if (DisplayPolicy.ALWAYS.equals(propertyInfo.getDisplayVisible())) {
							final Object value = objectHash.get(propertyInfo.getName());
							if (value == null) {
								itemsList.add("");
							} else {
								if (value instanceof Object[]) {
									final StringBuffer buff = new StringBuffer();
									final Object[] items = (Object[]) value;
									if (items.length > 0) {
										buff.append(items[0].toString());
									}
									for (int idx=1; idx < items.length; idx++) {
										buff.append(", ");
										buff.append(items[idx].toString());
									}
									itemsList.add(buff.toString());
								} else {
									itemsList.add(value.toString());
								}
							}
						}
					}
					final Object rowId = objectHash.get(rowIdPropertyName);
					if (firstId == null) {
						firstId = rowId;
					}
					grid.addItem(itemsList.toArray(), rowId);
				}
			} catch (UnsupportedOperationException | XmlRpcException e) {
				throw new RpcException(e);
			}
		} catch (RpcException e) {
			// FIXME error handling
		}
//		grid.setPageLength(0);
		grid.setSelectable(true);
		grid.setImmediate(true);
		grid.setSizeFull();
		if (grid.size() == 1) {
			grid.select(firstId);
		}
	}

	public Object getSelection() {
		return grid.getValue();
	}

	public String getRowIdName() {
		return rowIdPropertyName;
	}

}
