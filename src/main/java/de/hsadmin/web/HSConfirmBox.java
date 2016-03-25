package de.hsadmin.web;

import org.apache.log4j.Logger;
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
	
	private static final Logger LOG = Logger.getLogger(HSConfirmBox.class);
	
	private Button okButton, cancelButton;

	public HSConfirmBox(final IHSWindow parent, final String module, final String action, final HSAdminSession session) {
		okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		okButton.setClickShortcut(KeyCode.ENTER);
		okButton.addClickListener(new ClickListener() 
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) 
			{
				final IRemote iRemote = session.getModulesManager().proxy(module);
				final String runAsUser = session.getUser();
				final TicketService ticketService = session.getTicketService();
				boolean success = false;
				try {
					if (!parent.isValid()) {
						throw new RpcException("validation error");
					}
					final String ticket = ticketService.getServiceTicket(session.getGrantingTicket());
					try {
						if ("new".equals(action)) {
								iRemote.add(runAsUser, ticket, parent.getFormData());
								parent.reload();
								success = true;
						}
						if ("edit".equals(action)) {
							iRemote.update(runAsUser, ticket, parent.getFormData(), parent.getUniqueWhereSelector());
							parent.reload();
							success = true;
						}
						if ("delete".equals(action)) {
							iRemote.delete(runAsUser, ticket, parent.getUniqueWhereSelector());
							parent.reload();
							success = true;
						}
					} catch (final XmlRpcException e) {
						LOG.info("RPC Error", e);
						throw new RpcException(e);
					}
				} catch (final RpcException e) {
					final ErrorMessage componentError = new ErrorMessage() {
						private static final long serialVersionUID = 1L;
						@Override
						public ErrorLevel getErrorLevel() {
							return ErrorLevel.CRITICAL;
						}
						@Override
						public String getFormattedHtmlMessage() {
							return "<h3>Error</h3><p>" + e.getLocalizedMessage() + "</p>";
						}
					};
					parent.setComponentError(componentError);
					final Button btn = event.getButton();
					btn.setComponentError(componentError);
				}
				if (success) {
					((Window) parent).close();
				}
			}
		});
		cancelButton = new Button(I18N.getText("cancel"));
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
