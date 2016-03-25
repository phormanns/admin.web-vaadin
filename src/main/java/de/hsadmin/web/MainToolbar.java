package de.hsadmin.web;

import java.util.HashMap;

import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.hsadmin.rpc.HSAdminSession;

public class MainToolbar extends CustomComponent implements ClickListener {

	private static final long serialVersionUID = 1L;

	private final HSAdminSession session;

	public MainToolbar(final HSAdminSession session) {
		this.session = session;
		final Panel toolbar = new Panel();
		toolbar.setSizeFull();
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		final Component whitespace = new Panel();
		whitespace.setStyleName("borderless");
		layout.addComponent(whitespace);
		layout.setExpandRatio(whitespace, 1.0f);
		final String login = session.getUser();
		final Button profileBtn = new Button(login);
		profileBtn.setStyleName(ValoTheme.BUTTON_LINK);
		layout.addComponent(profileBtn);
		layout.setExpandRatio(profileBtn, 0.0f);
		profileBtn.setId("profile-btn");
		profileBtn.addClickListener(this);
		final Button logoutBtn = createButton("logout", "x");
		layout.addComponent(logoutBtn);
		layout.setExpandRatio(logoutBtn, 0.0f);
		toolbar.setContent(layout);
		setCompositionRoot(toolbar);
	}

	private Button createButton(final String name, final String icon) {
		final Button btn = new Button();
		btn.setId(name + "-btn");
		btn.setIcon(new ThemeResource("../icons/" + icon + "-icon.png"));
		btn.setDescription(I18N.getText(name + ".tooltip"));
		btn.setStyleName("borderless");
		btn.addClickListener(this);
		return btn;
	}

	@Override
	public void buttonClick(final ClickEvent event) {
		final String btnId = event.getComponent().getId();
		if ("logout-btn".equals(btnId)) {
			final VaadinRequest currentRequest = VaadinService.getCurrentRequest();
			final UI vaadinUI = getUI();
			vaadinUI.getSession().close();
			vaadinUI.getPage().setLocation(currentRequest.getContextPath());
		}
		if ("profile-btn".equals(btnId)) {
			final HashMap<String, String> where = new HashMap<String, String>();
			where.put("name", session.getUser());
			final Window window = new UserProfileWindow(session, where);
			if (window != null) {
				getUI().addWindow(window);
			}		
		}
	}
	
}
