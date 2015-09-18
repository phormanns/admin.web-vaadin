package de.hsadmin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.hsadmin.model.IRemote;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class PanelToolbar extends HorizontalLayout implements ClickListener {

	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;
	private final HSTab parent;
	private final String module;

	private Button newBtn, editBtn, deleteBtn, refreshBtn, helpBtn;
	private ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages");

	public PanelToolbar(String source, HSAdminSession session, HSTab parent) 
	{
		super();
		this.module = source;
		this.session = session;
		this.parent = parent;
		newBtn = createButton("New_" + source, "new", "New");
		addComponent(newBtn);
		editBtn = createButton("Edit_" + source, "edit", "Edit");
		addComponent(editBtn);
		deleteBtn = createButton("Delete_" + source, "trash", "Delete");
		addComponent(deleteBtn);
		refreshBtn = createButton("Refresh_" + source, "reload", "Refresh");
		addComponent(refreshBtn);
		helpBtn = createButton("Help", "question", "Help");
		addComponent(helpBtn);
	}

	private Button createButton(String name, String image, String tooltip) {
		Button btn = new Button();
		btn.setId(name + "-btn");
		if (image != null) {
			btn.setIcon(new ThemeResource("../icons/" + image + "-icon.png"));
		}
		String tooltipText;
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			tooltipText = resourceBundle.getString(tooltip);
		}catch(MissingResourceException e){
			tooltipText = tooltip;
		}
		btn.setDescription(tooltipText);
		btn.setStyleName("borderless");
		btn.addClickListener(this);
		return btn;
	}

	@Override
	public void buttonClick(ClickEvent event) 
	{
		String action = null;
		if (event.getButton().equals(newBtn)) {
			action = "new";
		} else if (event.getButton().equals(editBtn)) {
			action = "edit";
		} else if (event.getButton().equals(deleteBtn)) {
			action = "delete";
		} else if (event.getButton().equals(refreshBtn)) {
			action = "refresh";
		} else if (event.getButton().equals(helpBtn)) {
			action = "help";
		}
		if ("refresh".equals(action)) {
			parent.fillTable();
			return;
		}
		Object value = parent.getSelection();
		if (value == null) {
			if ("edit".equals(action) || "delete".equals(action)) {
				String errorMessage;
				try{
					errorMessage = resourceBundle.getString("emptySelectionMessage");
				}catch(MissingResourceException e){
					errorMessage = "please select a record to ";
				}
				UI.getCurrent().addWindow(new InfoWindow(errorMessage + action));
				return;
			}
		}
		final AbstractWindowFactory windowFactory = FactoryProducer.getWindowFactory("subwindow");
		String type = null;
		final String buttonId = event.getButton().getId();
		if (buttonId == null) {
			return;
		}
		type = buttonId.substring(buttonId.indexOf("_") + 1, buttonId.indexOf("-"));
		final Window window = windowFactory.getSubWindow(parent, type, action, session);
		if ("edit".equals(action) || "delete".equals(action)) {
			final IRemote remote = session.getModulesManager().proxy(module);
			final HashMap<String, String> whereParams = new HashMap<String, String>();
			whereParams.put(parent.getRowIdName(), value.toString());
			try {
				final TicketService ticketService = session.getTicketService();
				final String grantingTicket = session.getGrantingTicket();
				final String serviceTicket = ticketService.getServiceTicket(grantingTicket);
				final String user = session.getUser();
				final List<Map<String,Object>> list = remote.search(user, serviceTicket, whereParams);
				((IHSWindow) window).setFormData(list.get(0), whereParams);
			} catch (XmlRpcException | RpcException e) {
				e.printStackTrace();
			}
		}
		if (window != null) {
			UI.getCurrent().addWindow(window);
		}
	}

}
