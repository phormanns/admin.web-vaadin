package de.hsadmin.web;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.hsadmin.model.HSLocale;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.RpcException;

public class LoginWindow extends Window {

	private static final long serialVersionUID = 1L;
    final HSLocale locale = HSLocale.getHSLocale();
	final ResourceBundle resourceBundle;

	public LoginWindow(final HSAdminUI parent, final TicketService ticketService) {
		super("Login");
		center();
		setModal(true);
		setWidth("480px");
		locale.setLocale("en");   //Default English
		resourceBundle = ResourceBundle.getBundle("Messages", HSLocale.getHSLocale().getLocale());

		final FormLayout subContent = new FormLayout();
		subContent.setMargin(true);
		
		final TextField login = new TextField(resourceBundle.getString("user.name"));
		login.setWidth("100%");
		subContent.addComponent(login);
		login.focus();
		final PasswordField password = new PasswordField(resourceBundle.getString("password"));
		password.setWidth("100%");
		subContent.addComponent(password);
		final Label feedback = new Label("");
		feedback.setWidth("100%");
		feedback.setVisible(false);
		subContent.addComponent(feedback);
		feedback.setStyleName(ValoTheme.LABEL_FAILURE);
		
		final NativeSelect languageSelect = new NativeSelect("Language");
		// Add supported languages TODO: Maybe read it from an interface
		languageSelect.addItems("English", "Deutsch", "Español");
		languageSelect.setValue("English");
		languageSelect.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 2754712574220857944L;
            public void valueChange(ValueChangeEvent event) {
            	switch(languageSelect.getValue().toString()){
            	case "English":
            		locale.setLocale("en");
            		break;
            	case "Español":
            		locale.setLocale("es");
            		break;
            	case "Deutsch":
            		locale.setLocale("de");
            		break;
            	}
            }
        });
		subContent.addComponent(languageSelect);
		
		final Button okButton = new Button(resourceBundle.getString("login"));
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
						final String user = credentials.get(resourceBundle.getString("user.name"));
						final String loginUser = user.length() == 3 ? "hsh00-" + user : user;
						final String password = credentials.get(resourceBundle.getString("password"));
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

		setContent(subContent);
	}

}
