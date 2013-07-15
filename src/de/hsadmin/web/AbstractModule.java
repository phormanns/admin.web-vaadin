package de.hsadmin.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

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
	private Map<String,Map<String, Object>> moduleProps = null;

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
									Map<String, XmlrpcProperty> map = new HashMap<String, XmlrpcProperty>();
									genericForm.transferToHash(map, form,false);
									((InsertAble) thisModule).insertRow(map);
									componentFactory.loadData();
								} catch (HsarwebException e) {
									application.showHsarWebException(e);
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
			toolbar.setWidth("100%");
			Label space = new Label("  ");
			space.setWidth("100%");
			toolbar.addComponent(space);
			toolbar.setExpandRatio(space, 1.0f);
			createRunAsSelect(toolbar);
			createLogoutButton(toolbar, localeConfig.getText("logout"));
			layout.addComponent(toolbar);
		}
		layout.addComponent(component);
		layout.setExpandRatio(component, 1.0f);
	}

	protected void createLogoutButton(HorizontalLayout toolbar,
			final String buttonText) {
		Button btLogout = new Button(buttonText);
		btLogout.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				ExternalResource logoutLink = new ExternalResource("logout");
				application.getMainWindow().open(logoutLink);
			}
		});
		toolbar.addComponent(btLogout);
		ThemeResource icon = new ThemeResource("../runo/icons/16/cancel.png");
		btLogout.setIcon(icon);
	}

	private void createRunAsSelect(HorizontalLayout toolbar) throws UnsupportedOperationException, HsarwebException {
		selRunAs = new Select();
		selRunAs.setWidth(100.0f, Sizeable.UNITS_PIXELS);
		selRunAs.setImmediate(true);
		selRunAs.setNewItemsAllowed(true);
		selRunAs.setNullSelectionAllowed(false);
		selRunAs.addItem(application.getLogin());
		for (Object item : application.readSelectRunAsItems()) {
			selRunAs.addItem(item);
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
		selRunAs.setDescription(application.getLocaleConfig().getText("runas"));
		toolbar.addComponent(selRunAs);
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
	
	protected abstract void initModule() throws HsarwebException;

	public MainApplication getApplication() {
		return application;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	@Override
	public abstract ModuleConfig getModuleConfig();

	@Override
	public Map<String,Map<String,Object>> getModuleProps() {
		if (this.moduleProps == null) {
			this.moduleProps = (Map<String,Map<String,Object>>) getApplication().getModuleProps().get(getModuleConfig().getName());
		}
		return moduleProps;
	}
}
