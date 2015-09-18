package de.hsadmin.web;

import java.util.MissingResourceException;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class CustomerPanel extends CustomComponent implements IHSPanel, SelectedTabChangeListener {

	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;

	public CustomerPanel(HSAdminSession session, Object itemId) throws RpcException {
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
		String caption;
		TabSheet tabsheet = new TabSheet();
		tabsheet.setSizeFull();
		tabsheet.addSelectedTabChangeListener(this);
		tabsheet.addSelectedTabChangeListener(this);
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			caption = resourceBundle.getString("customer");
		}catch(MissingResourceException e){
			caption = "customer";
		}
		tabsheet.addTab(new GenericForm("customer", session, itemId, "name"), caption);
		HSTab usersTab = new HSTab("contact", session, "customer", itemId, "email");
		usersTab.fillTable();
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			caption = resourceBundle.getString("contact");
		}catch(MissingResourceException e){
			caption = "contact";
		}
		tabsheet.addTab(usersTab, caption);
		HSTab aliasTab = new HSTab("mandat", session, "customer", itemId, "mandatRef");
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			caption = resourceBundle.getString("mandat");
		}catch(MissingResourceException e){
			caption = "mandat";
		}
		tabsheet.addTab(aliasTab, caption);
		HSTab pacTab = new HSTab("pac", session, "customer", itemId, "name");
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			caption = resourceBundle.getString("pac");
		}catch(MissingResourceException e){
			caption = "pac";
		}
		tabsheet.addTab(pacTab, caption);
		return tabsheet;
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		final Component tab = event.getTabSheet().getSelectedTab();
		if (tab instanceof HSTab) {
			((HSTab) tab).fillTable();
		}
	}

}
