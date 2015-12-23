package de.hsadmin.web;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;

import de.hsadmin.model.IRemote;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.RpcException;

public class GenericForm extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	final private FormLayout formLayout;
	final private Map<String, IHSEditor> inputFields;

	public GenericForm(final String module, final HSAdminSession session, final Object itemId, String selectPropertyName) throws RpcException {
		formLayout = new FormLayout();
		formLayout.setMargin(true);
		inputFields = new HashMap<String, IHSEditor>();
		final Iterator<PropertyInfo> iterator = session.getModulesManager().module(module).properties();
		while (iterator.hasNext()) {
			final PropertyInfo propertyInfo = iterator.next();
			final String inputName = propertyInfo.getName().toLowerCase();
			final AbstractEditorFactory editorFactory = FactoryProducer.getEditorFactory(module);
			final IHSEditor field = editorFactory.getEditor("view", propertyInfo, inputName, session);
			inputFields.put(inputName, field);
			formLayout.addComponent(field);
		}
		setCompositionRoot(formLayout);
		
		final Hashtable<String, String> whereParams = new Hashtable<String, String>();
		whereParams.put(selectPropertyName, itemId.toString());
		
		try {
			final String grantingTicket = session.getGrantingTicket();
			final TicketService ticketService = session.getTicketService();
			final String user = session.getUser();
			final String serviceTicket = ticketService.getServiceTicket(grantingTicket);
			final IRemote proxy = session.getModulesManager().proxy(module);
			final List<Map<String,Object>> list = proxy.search(user, serviceTicket, whereParams);
			
			final Set<String> keySet = inputFields.keySet();
			for (String key : keySet) {
				final Object value = list.get(0).get(key);
				inputFields.get(key).setValue(value == null ? "" : value.toString());
			}
		} catch (XmlRpcException e) {
			throw new RpcException(e);
		}

		
	}

}
