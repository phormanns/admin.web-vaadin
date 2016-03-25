package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.model.IRemote;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.ModulesManager;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.RpcException;

public class EMailTargetEditor extends CustomComponent implements IHSEditor, ValueChangeListener {

	private static final long serialVersionUID = 1L;
	
	private final boolean isEditAble;
	private final PropertyInfo propertyInfo;
	private final List<Validator> validators;
	private final AbstractOrderedLayout layout;
	private final List<String> aliases;
	private final List<String> postboxes;


	public EMailTargetEditor(final String action, final PropertyInfo propertyInfo, final HSAdminSession session, final Map<String, String> whereContext) {
		this.isEditAble = PanelToolbar.ACTION_EDIT.equals(action) || PanelToolbar.ACTION_NEW.equals(action);
		this.propertyInfo = propertyInfo;
		this.validators = new ArrayList<>();
		this.setCaption(I18N.getText(propertyInfo.getName()));
		this.aliases = targetsSelect("emailalias", session, whereContext);
		this.postboxes = targetsSelect("user", session, whereContext);
		postboxes.removeAll(aliases);
		layout = new VerticalLayout();
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
				final String target = singleTarget.toString();
				layout.addComponent(new TargetRow(this, target));
			}
		}
		assertLastTargetRowIsEmpty();
	}
	
	public void assertLastTargetRowIsEmpty() {
		final int rowsCount = layout.getComponentCount();
		if (isEditAble) {
			if (rowsCount > 0) {
				final Component component = layout.getComponent(rowsCount - 1);
				if (! ((TargetRow) component).valueIsEmpty()) {
					layout.addComponent(new TargetRow(this));
				}
			} else {
				layout.addComponent(new TargetRow(this));
			}
		}
	}

	@Override
	public Object getValue() {
		final int numberOfChildren = layout.getComponentCount();
		final List<String> targetsList = new ArrayList<>();
		for (int idx = 0; idx < numberOfChildren; idx++) {
			final Component child = layout.getComponent(idx);
			if (child instanceof AbstractOrderedLayout) {
				final Component grandChild = ((AbstractOrderedLayout) child).getComponent(1);
				if (grandChild instanceof TextField) {
					targetsList.add(((TextField) grandChild).getValue()); 
				}
				if (grandChild instanceof NativeSelect) {
					targetsList.add(((NativeSelect) grandChild).getValue().toString()); 
				}
			}
		}
		return targetsList.toArray(new String[] { });
	}

	@Override
	public void addValidator(Validator validator) {
		validators.add(validator);
	}

	@Override
	public boolean isValid() {
		for (Validator vldr : validators) {
			try {
				vldr.validate(getValue());
			} catch (InvalidValueException e) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		assertLastTargetRowIsEmpty();
	}

	private List<String> targetsSelect(final String type, final HSAdminSession session, final Map<String, String> whereContext) {
		final List<String> selectList = new ArrayList<>();
		try {
			String pac = whereContext.get("pac");
			if (pac == null) {
				final Map<String, String> domainContext = new HashMap<>();
				domainContext.put("name", whereContext.get("domain"));
				final List<Map<String, Object>> domainResult = search("domain", session, domainContext);
				pac = (String) domainResult.get(0).get("pac");
			}
			final Map<String, String> pacContext = new HashMap<>();
			pacContext.put("pac", pac);
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

	private class TargetRow extends HorizontalLayout {
	
		private static final long serialVersionUID = 1L;

		private TargetRow(final EMailTargetEditor editor, final String target) {
			setWidth("100%");
			setSpacing(true);
			final NativeSelect targetTypeSelect = new NativeSelect();
			targetTypeSelect.addItems(new Object[] { I18N.getText("emailtarget.email"), I18N.getText("emailtarget.postbox"), I18N.getText("emailtarget.alias") });
			targetTypeSelect.setWidth("6.0em");
			targetTypeSelect.setValue("");
			addComponent(targetTypeSelect);
			setComponentAlignment(targetTypeSelect, Alignment.MIDDLE_LEFT);
			setExpandRatio(targetTypeSelect, 0.0f);
			AbstractComponent targetField = null;
			NativeSelect sel = null;
			if (aliases.contains(target)) {
				targetTypeSelect.setValue(I18N.getText("emailtarget.alias"));
				sel = new NativeSelect();
				sel.addItems(aliases);
				sel.setValue(target);
				sel.addValueChangeListener(editor);
				sel.setEnabled(isEditAble);
				targetField = sel;
			} else {
				if (postboxes.contains(target)) {
					targetTypeSelect.setValue(I18N.getText("emailtarget.postbox"));
					sel = new NativeSelect();
					sel.addItems(postboxes);
					sel.setValue(target);
					sel.addValueChangeListener(editor);
					sel.setEnabled(isEditAble);
					targetField = sel;
				} else {
					targetTypeSelect.setValue(I18N.getText("emailtarget.email"));
					targetField = new TextField();
					targetField.setEnabled(isEditAble);
					((TextField) targetField).setValue(target);
					((TextField) targetField).addValueChangeListener(editor);
				}
			}
			final AbstractComponent field = targetField;
			targetTypeSelect.setImmediate(true);
			targetTypeSelect.addValueChangeListener(new ValueChangeListener() {
	
				private static final long serialVersionUID = 1L;
	
				@Override
				public void valueChange(ValueChangeEvent event) {
					final TargetRow targetRow = (TargetRow) targetTypeSelect.getParent();
					final Object newValueObject = event.getProperty().getValue();
					if (targetRow.getComponentCount() == 2) {
						final Component component = targetRow.getComponent(1);
						targetRow.removeComponent(component);
					}
					if (newValueObject == null) { 
						layout.removeComponent(targetRow);
					} else 
					{
						final String newValue = newValueObject.toString();
						if (targetRow.getComponentCount() == 1) {
							AbstractComponent comp = null;
							if (I18N.getText("emailtarget.email").equals(newValue)) {
								final TextField textField = new TextField();
								textField.addValueChangeListener(editor);
								textField.setImmediate(true);
								textField.setEnabled(isEditAble);
								comp = textField;
							} else {
								if (I18N.getText("emailtarget.alias").equals(newValue) || I18N.getText("emailtarget.postbox").equals(newValue)) {
									final NativeSelect select = new NativeSelect();
									if (I18N.getText("emailtarget.alias").equals(newValue)) {
										select.addItems(aliases);
									} else {
										select.addItems(postboxes);
									}
									select.addValueChangeListener(editor);
									select.setImmediate(true);
									select.setEnabled(isEditAble);
									comp = select;
								} else {
									final TextField textField = new TextField();
									textField.addValueChangeListener(editor);
									textField.setImmediate(true);
									textField.setEnabled(isEditAble);
									comp = textField;
								}
							}
							comp.setWidth("100%");
							targetRow.addComponent(comp);
							targetRow.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
							targetRow.setExpandRatio(comp, 1.0f);
						}
					}
				}
				
			});
			targetTypeSelect.setEnabled(isEditAble);
			field.setWidth("100%");
			addComponent(field);
			setComponentAlignment(field, Alignment.MIDDLE_RIGHT);
			setExpandRatio(field, 1.0f);
		}

		public TargetRow(final EMailTargetEditor eMailTargetEditor) {
			this(eMailTargetEditor, "");
		}

		private boolean valueIsEmpty() {
			if (getComponentCount() == 2) {
				final Component component = getComponent(1);
				if (component instanceof TextField) {
					final String value = ((TextField) component).getValue();
					return value == null || value.isEmpty();
				}
				if (component instanceof NativeSelect) {
					final Object value = ((NativeSelect) component).getValue();
					return value == null || value.toString().isEmpty();
				}
			}
			return true;
		}
	}
	
}
