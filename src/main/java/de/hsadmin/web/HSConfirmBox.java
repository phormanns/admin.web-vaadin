package de.hsadmin.web;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.hsadmin.model.IRemote;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.RpcException;

public class HSConfirmBox extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	
	private Button okButton, cancelButton;
	private ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages");

	public HSConfirmBox(final GenericFormWindow parent, final String module, final String action, final HSAdminSession session) {
		okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		okButton.setClickShortcut(KeyCode.ENTER);
		okButton.addClickListener(new ClickListener() 
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				final IRemote iRemote = session.getModulesManager().proxy(module);
				final String runAsUser = session.getUser();
				final TicketService ticketService = session.getTicketService();
				boolean success = false;
				try {
					final String ticket = ticketService.getServiceTicket(session.getGrantingTicket());
					try {
						if ("new".equals(action)) {
								iRemote.add(runAsUser, ticket, parent.getFormData());
								parent.reload();
								success = true;
						}
						if ("edit".equals(action)) {
							iRemote.update(runAsUser, ticket, parent.getFormData(), parent.getSelector());
							parent.reload();
							success = true;
						}
						if ("delete".equals(action)) {
							iRemote.delete(runAsUser, ticket, parent.getSelector());
							parent.reload();
							success = true;
						}
					} catch (final XmlRpcException e) {
						throw new RpcException(e);
					}
				} catch (final RpcException e) {
					Button btn = event.getButton();
					btn.setComponentError(new ErrorMessage() {
						private static final long serialVersionUID = 1L;
						@Override
						public ErrorLevel getErrorLevel() {
							return ErrorLevel.CRITICAL;
						}
						@Override
						public String getFormattedHtmlMessage() {
							return e.getLocalizedMessage();
						}
					});
				}
				if (success) {
					((Window) parent).close();
				}
			}
		});
		String text;
		/*Try to get the translation from the properties file - if it doesn't 
		  exist, don't throw the error - Just print the normal text*/
		try{
			text = resourceBundle.getString("cancel");
		}catch(MissingResourceException e){
			text = "cancel";
		}
		cancelButton = new Button(text);
		cancelButton.setClickShortcut(KeyCode.ESCAPE);
		cancelButton.addClickListener(new ClickListener() 
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) 
			{
				((Window) parent).close();
			}
		});

		setSpacing(true);
		addComponent(okButton);
		addComponent(cancelButton);
	}

}
