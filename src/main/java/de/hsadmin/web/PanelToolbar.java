package de.hsadmin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public static final String ACTION_NEW = "new";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_DELETE = "delete";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_REFRESH = "refresh";
	public static final String ACTION_HELP = "help";

	private final HSAdminSession session;
	private final HSTab parent;
	private final String module;

	private final Button newBtn, editBtn, deleteBtn, refreshBtn, helpBtn;

	public PanelToolbar(final String source, final HSAdminSession session, final HSTab parent) 
	{
		super();
		this.module = source;
		this.session = session;
		this.parent = parent;
		newBtn = createButton("New_" + source, "new", "new.tooltip");
		addComponent(newBtn);
		editBtn = createButton("Edit_" + source, "edit", "edit.tooltip");
		addComponent(editBtn);
		deleteBtn = createButton("Delete_" + source, "trash", "delete.tooltip");
		addComponent(deleteBtn);
		refreshBtn = createButton("Refresh_" + source, "reload", "refresh.tooltip");
		addComponent(refreshBtn);
		helpBtn = createButton("Help", "question", "help.tooltip");
		addComponent(helpBtn);
	}

	private Button createButton(String name, String image, String tooltip) {
		Button btn = new Button();
		btn.setId(name + "-btn");
		if (image != null) {
			btn.setIcon(new ThemeResource("../icons/" + image + "-icon.png"));
		}
		btn.setDescription(I18N.getText(tooltip));
		btn.setStyleName("borderless");
		btn.addClickListener(this);
		return btn;
	}

	@Override
	public void buttonClick(final ClickEvent event) 
	{
		String action = null;
		if (event.getButton().equals(newBtn)) {
			action = ACTION_NEW;
		} else if (event.getButton().equals(editBtn)) {
			action = ACTION_EDIT;
		} else if (event.getButton().equals(deleteBtn)) {
			action = ACTION_DELETE;
		} else if (event.getButton().equals(refreshBtn)) {
			action = ACTION_REFRESH;
		} else if (event.getButton().equals(helpBtn)) {
			action = ACTION_HELP;
		}
		if (ACTION_REFRESH.equals(action)) {
			parent.fillTable();
			return;
		}
		final Object value = parent.getSelection();
		if (value == null) {
			if (ACTION_EDIT.equals(action) || ACTION_DELETE.equals(action)) {
				UI.getCurrent().addWindow(new InfoWindow(I18N.getText("emptySelectionMessage") + " " + action));
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
		if (ACTION_EDIT.equals(action) || ACTION_DELETE.equals(action)) {
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
		if (ACTION_NEW.equals(action)) {
			final Map<String, String> whereParams = new HashMap<String, String>();
			final Map<String, Object> parentParams = new HashMap<String, Object>();
			parentParams.put(parent.getSelectPropertyName(), parent.getSelectPropertyValue());
			((IHSWindow) window).setFormData(parentParams, whereParams);
		}
		if (window != null) {
			UI.getCurrent().addWindow(window);
		}
	}

}
