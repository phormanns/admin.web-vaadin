package de.hsadmin.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.enums.ReadWritePolicy;

public class GenericFormWindow extends Window implements IHSWindow {

	private static final long serialVersionUID = 1L;

	final private FormLayout contentForm;
	final private Map<String, IHSEditor> inputFields;
	final private HSTab parent;
	
	private Map<String, String> selector;
	
	public GenericFormWindow(final HSTab parent, final String module, final String action, final HSAdminSession session) 
	{
		super(action + " " + module);
		this.parent = parent;
		center();
		setModal(true);
		setWidth("480px");
		inputFields = new HashMap<String, IHSEditor>();
		contentForm = new FormLayout();
		contentForm.setMargin(true);

		final Iterator<PropertyInfo> iterator = session.getModulesManager().module(module).properties();
		while (iterator.hasNext()) {
			final PropertyInfo propertyInfo = iterator.next();
			if ("new".equals(action) && ReadWritePolicy.READ.equals(propertyInfo.getReadwriteable())) {
				continue;
			}
			final String inputName = propertyInfo.getName();
			final AbstractEditorFactory editorFactory = FactoryProducer.getEditorFactory(module);
			final IHSEditor field = editorFactory.getEditor(action, propertyInfo, inputName, session);
			inputFields.put(inputName, field);
			contentForm.addComponent(field);
		}
		contentForm.addComponent(new HSConfirmBox(this, module, action, session));
		setContent(contentForm);
	}

	@Override
	public void setFormData(Map<String, Object> valuesMap, Map<String, String> selector) {
		this.selector = selector;
		final Set<String> keySet = inputFields.keySet();
		for (String key : keySet) {
			final Object value = valuesMap.get(key);
			inputFields.get(key).setValue(value == null ? "" : value.toString());
		}
	}

	@Override
	public Map<String, Object> getFormData() {
		Map<String, Object> formData = new HashMap<String, Object>(); 
		final Set<String> keySet = inputFields.keySet();
		for (String key : keySet) {
			formData.put(key, inputFields.get(key).getValue());
		}
		return formData;
	}

	@Override
	public Map<String, String> getSelector() {
		return selector;
	}

	@Override
	public void reload() {
		parent.fillTable();
	}

}
