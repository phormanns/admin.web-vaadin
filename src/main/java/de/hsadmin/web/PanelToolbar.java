package de.hsadmin.web;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.hsadmin.rpc.HSAdminSession;

public class PanelToolbar extends HorizontalLayout implements ClickListener {

	private static final long serialVersionUID = 1L;
	
	private final HSAdminSession session;

	private Button newBtn, editBtn, deleteBtn, refreshBtn, helpBtn;


	public PanelToolbar(String source, HSAdminSession session) 
	{
		super();
		this.session = session;
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
		btn.setDescription(tooltip);
		btn.setStyleName("borderless");
		btn.addClickListener(this);
		return btn;
	}

	@Override
	public void buttonClick(ClickEvent event) 
	{
		final AbstractFactory windowFactory = FactoryProducer.getFactory("subwindow");
		String type = null;
		String action = null;
		final String buttonId = event.getButton().getId();
		if (buttonId == null) {
			return;
		}
		type = buttonId.substring(buttonId.indexOf("_") + 1, buttonId.indexOf("-"));
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
		Window window = (Window) windowFactory.getSubWindow(type, action, session);
		if (window != null) {
			UI.getCurrent().addWindow(window);
		}
	}

}
