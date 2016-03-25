package de.hsadmin.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.RpcException;

public class UserProfileWindow extends Window implements IHSWindow {

	private static final long serialVersionUID = 1L;

	final private FormLayout contentForm;
	final private Map<String, IHSEditor> inputFields;
	
	private Map<String, String> uniqueRecordSelect;
	
	public UserProfileWindow(final HSAdminSession session, final Map<String, String> whereContext) 
	{
		super(I18N.getText("edit") + " " + I18N.getText("user"));
		center();
		setModal(true);
		setWidth("640px");
		inputFields = new HashMap<String, IHSEditor>();
		contentForm = new FormLayout();
		contentForm.setMargin(true);

		final Iterator<PropertyInfo> iterator = session.getModulesManager().module("user").properties();
		final Collection<String> visibleFields = Arrays.asList(new String[] { "name", "password", "pac", "comment", "shell", "homedir" });
		while (iterator.hasNext()) {   // "name", "password", "pac", "comment", "shell", "homedir"
			final PropertyInfo propertyInfo = iterator.next();
			final String inputName = propertyInfo.getName();
			if (visibleFields.contains(inputName)) {
				final IEditorFactory editorFactory = FactoryProducer.getEditorFactory("user");
				final IHSEditor field = editorFactory.getEditor(inputName.equals("password") ? "edit" : "view", propertyInfo, session, whereContext);
				inputFields.put(inputName, field);
				contentForm.addComponent(field);
			}
		}
		contentForm.addComponent(new HSConfirmBox(this, "user", "edit", session));
		try {
			final List<Map<String,Object>> list = session.getModulesManager().proxy("user").search(session.getUser(), session.getTicketService().getServiceTicket(session.getGrantingTicket()), whereContext);
			if (list.size() == 1) {
				setFormData(list.get(0), whereContext);
				setContent(contentForm);
			}
		} catch (XmlRpcException | RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setFormData(final Map<String, Object> valuesMap, final Map<String, String> uniqueWhereSelector) {
		this.uniqueRecordSelect = uniqueWhereSelector;
		final Set<String> keySet = inputFields.keySet();
		for (final String key : keySet) {
			inputFields.get(key).setValues(valuesMap);
		}
	}

	@Override
	public Map<String, Object> getFormData() {
		final Map<String, Object> formData = new HashMap<String, Object>(); 
		final Set<String> keySet = inputFields.keySet();
		for (String key : keySet) {
			final IHSEditor ihsEditor = inputFields.get(key);
			if (ihsEditor.isEnabled()) {
				formData.put(key, ihsEditor.getValue());
			}
		}
		return formData;
	}

	@Override
	public boolean isValid() {
		boolean valid = true;
		for (IHSEditor editor : inputFields.values()) {
			valid &= editor.isValid();
		}
		return valid;
	}

	@Override
	public Map<String, String> getUniqueWhereSelector() {
		return uniqueRecordSelect;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

}
