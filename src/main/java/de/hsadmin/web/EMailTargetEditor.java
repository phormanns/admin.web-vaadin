package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import de.hsadmin.model.IRemote;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.ModulesManager;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.RpcException;

public class EMailTargetEditor extends CustomComponent implements IHSEditor {

	private static final long serialVersionUID = 1L;
	
	private final PropertyInfo propertyInfo;
	private final List<Validator> validators;
	private final FormLayout layout;
	private final List<String> aliases;
	private final List<String> postboxes;

	public EMailTargetEditor(final PropertyInfo propertyInfo, final HSAdminSession session, final Map<String, String> whereContext) {
		this.propertyInfo = propertyInfo;
		this.validators = new ArrayList<>();
		this.setCaption(I18N.getText(propertyInfo.getName()));
		this.aliases = targetsSelect("emailalias", session, whereContext);
		this.postboxes = targetsSelect("user", session, whereContext);
		layout = new FormLayout();
		layout.setMargin(true);
		layout.setCaption(I18N.getText(propertyInfo.getName()));
		setCompositionRoot(layout);
	}
	
	@Override
	public void setValues(Map<String, Object> valuesMap) {
		final Object targetValues = valuesMap.get(propertyInfo.getName());
		if (targetValues instanceof Object[]) {
			final Object[] targetsArray = (Object[]) targetValues;
			layout.removeAllComponents();
			for (final Object singleTarget : targetsArray) {
				final TextField field = new TextField();
				final String target = singleTarget.toString();
				String caption = I18N.getText("emailtarget.email");
				if (postboxes.contains(target)) {
					caption = I18N.getText("emailtarget.postbox");
				}
				if (aliases.contains(target)) {
					caption = I18N.getText("emailtarget.alias");
				}
				field.setCaption(caption);
				field.setValue(target);
				field.setWidth("100%");
				layout.addComponent(field);
			}
		}
		System.out.println("setValues() value: " + targetValues);
	}

	@Override
	public Object getValue() {
		System.out.println("getValue()");
		int numberOfChildren = layout.getComponentCount();
		final List<String> targetsList = new ArrayList<>();
		for (int idx = 0; idx < numberOfChildren; idx++) {
			final Component child = layout.getComponent(idx);
			if (child instanceof TextField) {
				targetsList.add(((TextField) child).getValue()); 
			}
		}
		return targetsList.toArray(new String[] { });
	}

	@Override
	public void addValidator(Validator validator) {
		System.out.println("addValidator()");
		validators.add(validator);
	}

	@Override
	public boolean isValid() {
		System.out.println("isValid()");
		for (Validator vldr : validators) {
			try {
				vldr.validate(getValue());
			} catch (InvalidValueException e) {
				return false;
			}
		}
		return true;
	}

	private List<String> targetsSelect(final String type, final HSAdminSession session, final Map<String, String> whereContext) {
		final List<String> selectList = new ArrayList<>();
		try {
			final Map<String, String> domainContext = new HashMap<>();
			domainContext.put("name", whereContext.get("domain"));
			final List<Map<String, Object>> domainResult = search("domain", session, domainContext);
			final Map<String, String> pacContext = new HashMap<>();
			pacContext.put("pac", (String) domainResult.get(0).get("pac"));
			final List<Map<String, Object>> list = search(type, session, pacContext);
			for (Map<String, Object> usr : list) {
				selectList.add(usr.get("name").toString());
			}
		} catch (RpcException | XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectList;
	}

	private List<Map<String, Object>> search(final String type,
			final HSAdminSession session,
			final Map<String, String> whereContext) throws RpcException, XmlRpcException 
	{
		final String grantingTicket = session.getGrantingTicket();
		final TicketService ticketService = session.getTicketService();
		final String serviceTicket = ticketService.getServiceTicket(grantingTicket);
		final ModulesManager modulesManager = session.getModulesManager();
		final IRemote proxy = modulesManager.proxy(type);
		final String user = session.getUser();
		return proxy.search(user, serviceTicket, whereContext);
	}
	
}
