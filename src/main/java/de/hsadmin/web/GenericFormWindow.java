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
	
	private Map<String, String> uniqueRecordSelect;
	
	public GenericFormWindow(final HSTab parent, final String module, final String action, final HSAdminSession session, final Map<String, String> whereContext) 
	{
		super(I18N.getText(action) + " " + I18N.getText(module));
		this.parent = parent;
		center();
		setModal(true);
		setWidth("640px");
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
			final IEditorFactory editorFactory = FactoryProducer.getEditorFactory(module);
			final IHSEditor field = editorFactory.getEditor(action, propertyInfo, session, whereContext);
			inputFields.put(inputName, field);
			contentForm.addComponent(field);
		}
		contentForm.addComponent(new HSConfirmBox(this, module, action, session));
		setContent(contentForm);
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
		Map<String, Object> formData = new HashMap<String, Object>(); 
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
	public Map<String, String> getUniqueWhereSelector() {
		return uniqueRecordSelect;
	}

	@Override
	public void reload() {
		parent.fillTable();
	}

	@Override
	public boolean isValid() {
		boolean valid = true;
		for (IHSEditor editor : inputFields.values()) {
			valid &= editor.isValid();
		}
		return valid;
	}

}
