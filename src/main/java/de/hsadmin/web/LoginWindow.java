package de.hsadmin.web;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.RpcException;

public class LoginWindow extends Window implements IHSWindow {

	private static final long serialVersionUID = 1L;

	public LoginWindow(final HSAdminUI parent, final TicketService ticketService) {
		super("Login");
		center();
		setModal(true);
		setWidth("480px");

		final FormLayout subContent = new FormLayout();
		subContent.setMargin(true);
		
		final TextField login = new TextField("login");
		subContent.addComponent(login);
		final PasswordField password = new PasswordField("password");
		subContent.addComponent(password);
		
		
		final Button okButton = new Button("OK");
		okButton.addClickListener(new ClickListener() 
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				final HasComponents formLayout = okButton.getParent();
				if (formLayout != null) {
					
					final FormLayout form = (FormLayout) formLayout;
					final int count = form.getComponentCount();
					final Map<String, String> credentials = new HashMap<String, String>();
					for (int idx=0; idx < count; idx++) {
						final Component component = form.getComponent(idx);
						if (component instanceof AbstractTextField) {
							final AbstractTextField tf = (AbstractTextField) component;
							if (tf.isEnabled() && tf.isValid()) {
								final String key = tf.getCaption();
								final String value = tf.getValue();
								credentials.put(key, value);
							}
						}
					}
					try {
						final String user = credentials.get("login");
						final String password = credentials.get("password");
						final String grantingTicket = ticketService.getGrantingTicket(user, password);
						if (grantingTicket != null && !grantingTicket.isEmpty()) {
							parent.setGrantingTicket(grantingTicket, user);
							final HasComponents window = formLayout.getParent();
							if (window != null) {
								((Window) window).close();
							}
						}
					} catch (RpcException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		subContent.addComponent(okButton);

		setContent(subContent);
	}
	
}
