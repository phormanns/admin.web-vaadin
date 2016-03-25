package de.hsadmin.web;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.hsadmin.model.TicketService;

@Title("HSAdmin Web")
@Theme(ValoTheme.THEME_NAME)
public class HSAdminUI extends UI {

	private static final long serialVersionUID = 1L;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = HSAdminUI.class)
    public static class Servlet extends VaadinServlet {

		private static final long serialVersionUID = 1L;
		
    }

	private VerticalLayout layout;
	private TicketService ticketService;

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		layout = new VerticalLayout();
		layout.setSizeFull();
		
		ticketService = new TicketService();
		UI.getCurrent().addWindow(new LoginWindow(this, ticketService));
		
		setContent(layout);
	}

	public void setGrantingTicket(String grantingTicket, String username) {
		layout.addComponent(new MainWindow(ticketService, grantingTicket, username));
	}
}
