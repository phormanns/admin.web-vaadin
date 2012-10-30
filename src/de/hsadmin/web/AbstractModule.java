package de.hsadmin.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.hsadmin.web.config.ComponentFactory;
import de.hsadmin.web.config.LocaleConfig;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.vaadin.GenericForm;
import de.hsadmin.web.vaadin.TableComponentFactory;

public abstract class AbstractModule implements Module, Serializable {

	private static final long serialVersionUID = 1L;
	
	private MainApplication application;
	private VerticalLayout layout;
	private Component component;
	private ComponentFactory componentFactory;
	private Select selRunAs;

	public Component getComponent() {
		return layout;
	}

	public void reload() throws HsarwebException {
		if (selRunAs != null) {
			selRunAs.select(application.getRunAs());
			selRunAs.setScrollToSelectedItem(true);
		}
		componentFactory.loadData();
	}

	private void initLayout()  throws HsarwebException {
		layout = new VerticalLayout();
		layout.setSizeFull();
		final Module thisModule = this;
		final ModuleConfig moduleConfig = getModuleConfig();
		final LocaleConfig localeConfig = application.getLocaleConfig();
		if (this instanceof SearchAble || this instanceof InsertAble ||
				!("USER".equals(application.getLoginUserRole()) || "NONE".equals(application.getLoginUserRole()))) {
			HorizontalLayout toolbar = new HorizontalLayout();
			if (!("USER".equals(application.getLoginUserRole()) || "NONE".equals(application.getLoginUserRole()))) {
				selRunAs = new Select();
				selRunAs.setWidth(100.0f, Sizeable.UNITS_PIXELS);
				selRunAs.setImmediate(true);
				selRunAs.setNewItemsAllowed(false);
				selRunAs.setNullSelectionAllowed(false);
				if (!application.getLoginUserRole().startsWith("PAC")) {
//					if (application.getLoginUserRole().startsWith("PAC")) {
//						
//					}
					selRunAs.addItem(application.getLogin());
					Object custListObj = application.getRemote().callSearch("member", new HashMap<String, String>());
					if (custListObj instanceof Object[]) {
						Object[] custList = (Object[]) custListObj;
						for (Object custObj : custList) {
							if (custObj instanceof Map<?, ?>) {
								Map<?, ?> custHash = (Map<?, ?>)custObj;
								selRunAs.addItem(custHash.get("membercode"));
							}
						}
					}
				}
				Object pacListObj = application.getRemote().callSearch("pac", new HashMap<String, String>());
				if (pacListObj instanceof Object[]) {
					Object[] pacList = (Object[]) pacListObj;
					for (Object pacObj : pacList) {
						if (pacObj instanceof Map<?, ?>) {
							Map<?, ?> pacHash = (Map<?, ?>)pacObj;
							selRunAs.addItem(pacHash.get("name"));
						}
					}
				}
				selRunAs.select(application.getRunAs());
				selRunAs.setScrollToSelectedItem(true);
				selRunAs.addListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						Property property = event.getProperty();
						application.setRunAs(property.getValue().toString());
					}
				});
				toolbar.addComponent(selRunAs);
			}
			if (this instanceof InsertAble) {
				Button btNew = new Button(moduleConfig.getLabel("new"));
				ThemeResource icon = new ThemeResource("../runo/icons/16/document-add.png");
				btNew.setIcon(icon);
				btNew.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					private Window childWindow;
					@Override
					public void buttonClick(ClickEvent event) {
						final GenericForm genericForm = new GenericForm(thisModule, null);
						final Form form = genericForm.createAddForm();
						childWindow = new Window(localeConfig.getText("new"));
						childWindow.setWidth(640.0f, Sizeable.UNITS_PIXELS);
						VerticalLayout vLayout = new VerticalLayout();
						vLayout.setMargin(true);
						vLayout.setSpacing(true);
						vLayout.addComponent(form);
						HorizontalLayout hLayout = new HorizontalLayout();
						Button btSaveRow = new Button(localeConfig.getText("save"));
						btSaveRow.addListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;
							@Override
							public void buttonClick(ClickEvent event) {
								application.getMainWindow().removeWindow(childWindow);
								try {
									Map<String, String> map = new HashMap<String, String>();
									genericForm.transferToHash(map, form);
									((InsertAble) thisModule).insertRow(map);
									componentFactory.loadData();
								} catch (HsarwebException e) {
									application.showUserException(e);
								}
							}
						});
						Button btAbort = new Button(localeConfig.getText("abort"));
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
				toolbar.addComponent(btNew);
			}
//			if (this instanceof SearchAble) {
//				Button btSearch = new Button("search");
//				toolbar.addComponent(btSearch);
//			}
			layout.addComponent(toolbar);
		}
		layout.addComponent(component);
		layout.setExpandRatio(component, 1.0f);
	}


	public void setApplication(MainApplication app) throws HsarwebException {
		application = app;
		initModule();
		if (componentFactory == null) {
			componentFactory = new TableComponentFactory(this);
		}
		component = (Component) componentFactory.initComponent();
		initLayout();
	}
	
	protected abstract void initModule();

	public MainApplication getApplication() {
		return application;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	@Override
	public abstract ModuleConfig getModuleConfig();

}
