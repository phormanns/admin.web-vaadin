package de.hsadmin.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.hsadmin.web.config.ComponentFactory;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.GenericForm;
import de.hsadmin.web.vaadin.PasswordPropertyFieldFactory;

public class HomeModule extends AbstractModule implements ComponentFactory, UpdateAble {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;
	private MainApplication application;

	public HomeModule() {
		super();
		setComponentFactory(this);
	}
	
	public void updateRow(Map<String, String> paramHash) throws HsarwebException {
		Map<String, String> whereHash = new HashMap<String, String>();
		String idKey = findIdKey();
		whereHash.put(idKey, paramHash.get(idKey));
		paramHash.remove(idKey);
		application.getRemote().callUpdate(moduleConfig.getRemoteName(), paramHash, whereHash);
	}

	private String findIdKey() {
		List<PropertyConfig> propertyList = getModuleConfig().getPropertyList();
		String idKey = null;
		for (PropertyConfig propConf : propertyList) {
			PropertyTableColumn propTableColumn = propConf.getPropTableColumn();
			if (PropertyTableColumn.INTERNAL_KEY == propTableColumn) {
				idKey = propConf.getId();
				return idKey;
			}
		}
		return idKey;
	}
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

	@Override
	protected void initModule() {
		application = getApplication();
		moduleConfig = new ModuleConfig("home", application.getLocale());
		moduleConfig.setRemoteName("user");
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN);
		pacProp.setReadOnly(true);
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
		PropertyConfig useridProp = new PropertyConfig(moduleConfig, "userid", Long.class, PropertyTableColumn.HIDDEN);
		useridProp.setReadOnly(true);
		PropertyConfig nameProp = new PropertyConfig(moduleConfig, "name", String.class);
		nameProp.setReadOnly(true);
		PropertyConfig passwordProp = new PropertyConfig(moduleConfig, "password", String.class, PropertyTableColumn.NONE, new PasswordPropertyFieldFactory(this));
		PropertyConfig commentProp = new PropertyConfig(moduleConfig, "comment", String.class);
		commentProp.setReadOnly(true);
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(pacProp);
		moduleConfig.addProperty(useridProp);
		moduleConfig.addProperty(nameProp);
		moduleConfig.addProperty(commentProp);
		moduleConfig.addProperty(passwordProp);
	}

	@Override
	public Object initComponent() throws HsarwebException {
		final Module thisModule = this;
		VerticalLayout layout = new VerticalLayout();
		Button button = new Button(moduleConfig.getLabel("change_password"));
		ThemeResource icon = new ThemeResource(moduleConfig.getLabel("change_password_icon"));
		button.setIcon(icon);
		Map<String, String> whereHash = new HashMap<String, String>();
		whereHash.put("name", application.getLogin());
		Long key = -1L;
		try {
			Object object = application.getRemote().callSearch(moduleConfig.getRemoteName(), whereHash);
			if (object instanceof Object[] && ((Object[]) object).length > 0) {
				key = Long.parseLong((String) ((Map<?, ?>) ((Object[]) object)[0]).get("id"));
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
			getApplication().showSystemException(e);
		}
		button.setData(key);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			private Window childWindow;
			@Override
			public void buttonClick(ClickEvent event) {
				Long data = (Long) event.getButton().getData();
				final GenericForm genericForm = new GenericForm(thisModule, data);
				final Form form = genericForm.createUpdateForm();
				childWindow = new Window(moduleConfig.getLabel("update"));
				childWindow.setWidth(640.0f, Sizeable.UNITS_PIXELS);
				VerticalLayout vLayout = new VerticalLayout();
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				vLayout.addComponent(form);
				HorizontalLayout hLayout = new HorizontalLayout();
				Button btSaveRow = new Button(moduleConfig.getLabel("save"));
				btSaveRow.setData(data);
				btSaveRow.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						application.getMainWindow().removeWindow(childWindow);
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", ((Long) event.getButton().getData()).toString());
							Iterator<Component> componentIterator = form.getLayout().getComponentIterator();
							while (componentIterator.hasNext()) {
								Component component = (Component) componentIterator.next();
								if (component instanceof AbstractComponent) {
									Object data = ((AbstractComponent) component).getData();
									String propName = (String) data;
									if ("password".equals(propName)) {
										PropertyConfig property = moduleConfig.getProperty(propName);
										map.put(propName, property.getPropFieldFactory().getValue(property, component));
									}
								}
							}
							((UpdateAble) thisModule).updateRow(map);
						} catch (HsarwebException e) {
							application.showUserException(e);
						}
					}
				});
				Button btAbort = new Button(moduleConfig.getLabel("abort"));
				btAbort.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						application.getMainWindow().removeWindow(childWindow);
					}
				});
				hLayout.addComponent(btSaveRow);
				hLayout.addComponent(btAbort);
				vLayout.addComponent(hLayout);
				childWindow.setContent(vLayout);
				childWindow.setModal(true);
				application.getMainWindow().addWindow(childWindow);
			}
		});
		layout.addComponent(button);
		return layout;
	}

	@Override
	public void loadData() throws HsarwebException {
		
	}

}
