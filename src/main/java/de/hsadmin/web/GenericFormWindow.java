package de.hsadmin.web;

import java.util.Iterator;

import com.vaadin.data.Validator;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.RpcException;
import de.hsadmin.rpc.enums.ReadWritePolicy;

public class GenericFormWindow extends Window implements IHSWindow {

	private static final long serialVersionUID = 1L;

	public GenericFormWindow(final String module, final String action, final HSAdminSession session) 
	{
		super(action + " " + module);
		center();
		setModal(true);
		setWidth("480px");

		final FormLayout subContent = new FormLayout();
		subContent.setMargin(true);

		final Iterator<PropertyInfo> iterator = session.getModulesManager().module(module).properties();
		while (iterator.hasNext()) {
			final PropertyInfo propertyInfo = iterator.next();
			final TextField field = new TextField(propertyInfo.getName());
			if (isWriteAble(propertyInfo, action)) {
				final String regexp = propertyInfo.getValidationRegexp();
				final int minLength = propertyInfo.getMinLength();
				final int maxLength = propertyInfo.getMaxLength();
				field.addValidator(new Validator() {
					private static final long serialVersionUID = 1L;
					@Override
					public void validate(Object value) throws InvalidValueException {
						final String inputString = (String) value;
						if (!inputString.matches(regexp)) {
							throw new InvalidValueException("input must match " + regexp);
						}
						if (inputString.length() < minLength) {
							throw new InvalidValueException("minimal length is " + minLength);
						}
						if (inputString.length() > maxLength) {
							throw new InvalidValueException("maximal length is " + maxLength);
						}
					}
				});
			} else {
				field.setEnabled(false);
			}
			subContent.addComponent(field);
		}
		final TicketService ticketService = session.getTicketService();
		final String grantingTicket = session.getGrantingTicket();
		try {
			subContent.addComponent(new HSConfirmBox(session.getModulesManager().proxy(module), action, session.getUser(), ticketService.getServiceTicket(grantingTicket)));
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContent(subContent);
	}

	private boolean isWriteAble(PropertyInfo propertyInfo, String action) {
		return (ReadWritePolicy.WRITEONCE.equals(propertyInfo.getReadwriteable()) && action.equals("new")) ||
				 (ReadWritePolicy.READWRITE.equals(propertyInfo.getReadwriteable()) && ( action.equals("edit") || action.equals("new") ));
	}

}
