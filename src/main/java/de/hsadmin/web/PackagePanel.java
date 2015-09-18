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

public class PackagePanel extends CustomComponent implements IHSPanel, SelectedTabChangeListener {

	private static final long serialVersionUID = 5397852733862398775L;
	
	private final HSAdminSession session;

	public PackagePanel(HSAdminSession session, Object itemId) throws RpcException {
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
		final TabSheet tabsheet = new TabSheet();
		tabsheet.addSelectedTabChangeListener(this);
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			caption = resourceBundle.getString("pac");
		}catch(MissingResourceException e){
			caption = "pac";
		}
		tabsheet.addTab(new GenericForm("pac", session, itemId, "name"), caption);
		final HSTab usersTab = new HSTab("user", session, "pac", itemId, "name");
		usersTab.fillTable();
		try{
			caption = resourceBundle.getString("user");
		}catch(MissingResourceException e){
			caption = "user";
		}
		tabsheet.addTab(usersTab, caption);
		final HSTab aliasTab = new HSTab("emailalias", session, "pac", itemId, "name");
		try{
			caption = resourceBundle.getString("emailalias");
		}catch(MissingResourceException e){
			caption = "emailalias";
		}
		tabsheet.addTab(aliasTab, caption);

		final HSTab domainTab = new HSTab("domain", session, "pac", itemId, "name");
		try{
			caption = resourceBundle.getString("domain");
		}catch(MissingResourceException e){
			caption = "domain";
		}
		tabsheet.addTab(domainTab, caption);

		final HSTab mysqluserTab = new HSTab("mysqluser", session, "pac", itemId, "name");
		try{
			caption = resourceBundle.getString("mysqluser");
		}catch(MissingResourceException e){
			caption = "mysqluser";
		}
		tabsheet.addTab(mysqluserTab, caption);
		final HSTab mysqldbTab = new HSTab("mysqldb", session, "pac", itemId, "name");
		try{
			caption = resourceBundle.getString("mysqldb");
		}catch(MissingResourceException e){
			caption = "mysqldb";
		}
		tabsheet.addTab(mysqldbTab, caption);
		final HSTab postgresqluserTab = new HSTab("postgresqluser", session, "pac", itemId, "name");
		try{
			caption = resourceBundle.getString("postgresqluser");
		}catch(MissingResourceException e){
			caption = "postgresqluser";
		}
		tabsheet.addTab(postgresqluserTab, caption);
		final HSTab postgresqldbTab = new HSTab("postgresqldb", session, "pac", itemId, "name");
		try{
			caption = resourceBundle.getString("postgresqldb");
		}catch(MissingResourceException e){
			caption = "postgresqldb";
		}
		tabsheet.addTab(postgresqldbTab, caption);
		
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
