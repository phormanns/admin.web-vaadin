package de.hsadmin.web;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.RpcException;

public class LoginWindow extends Window {

	private static final long serialVersionUID = 1L;

	public LoginWindow(final HSAdminUI parent, final TicketService ticketService) {
		super(I18N.getText("login.title"));
		center();
		setModal(true);
		setWidth("480px");

		final FormLayout subContent = new FormLayout();
		subContent.setMargin(true);
		
		final TextField login = new TextField(I18N.getText("user.name"));
		login.setWidth("100%");
		subContent.addComponent(login);
		login.focus();
		final PasswordField password = new PasswordField(I18N.getText("password"));
		password.setWidth("100%");
		subContent.addComponent(password);
		final Label feedback = new Label("");
		feedback.setWidth("100%");
		feedback.setVisible(false);
		subContent.addComponent(feedback);
		feedback.setStyleName(ValoTheme.LABEL_FAILURE);
		
		final Button okButton = new Button(I18N.getText("login.button"));
		okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		okButton.setClickShortcut(KeyCode.ENTER);
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
						final String user = credentials.get(I18N.getText("user.name"));
						final String loginUser = user.length() == 3 ? "hsh00-" + user : user;
						final String password = credentials.get(I18N.getText("password"));
						final String grantingTicket = ticketService.getGrantingTicket(loginUser, password);
						if (grantingTicket != null && !grantingTicket.isEmpty()) {
							feedback.setValue("successful login");
							feedback.setVisible(true);
							parent.setGrantingTicket(grantingTicket, loginUser);
							final HasComponents window = formLayout.getParent();
							if (window != null) {
								((Window) window).close();
							}
						} else {
							feedback.setValue("login failed, please retry");
							feedback.setVisible(true);
							((Window)subContent.getParent()).markAsDirty();
						}
					} catch (RpcException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		subContent.addComponent(okButton);
		final Link linkToOld = new Link(I18N.getText("main.panel.link_to_old"), new ExternalResource("https://admin.hostsharing.net/"));
		subContent.addComponent(linkToOld);

		setContent(subContent);
	}

}
