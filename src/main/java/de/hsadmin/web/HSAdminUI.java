package de.hsadmin.web;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("HSAdmin Web")
@Theme("valo")
public class HSAdminUI extends UI {

	private static final long serialVersionUID = 1L;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = HSAdminUI.class)
    public static class Servlet extends VaadinServlet {

		private static final long serialVersionUID = 1L;
		
    }

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		AbstractLayout layout = new VerticalLayout();
		
		layout.setSizeFull();
		layout.addComponent(new MainWindow());
		setContent(layout);
	}
}