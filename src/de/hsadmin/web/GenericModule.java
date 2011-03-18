package de.hsadmin.web;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

import de.hsadmin.web.config.LocaleConfig;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFormField;
import de.hsadmin.web.config.PropertyTableColumn;

public abstract class GenericModule implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

	private VerticalLayout layout;
	private Table table;
	private MainApplication application;

	public void setApplication(MainApplication app) throws HsarwebException {
		application = app;
		initModule();
		initTable();
		initLayout();
	}
	
	public MainApplication getApplication() {
		return application;
	}

	public abstract ModuleConfig getModuleConfig();

	protected abstract void initModule();

	public Component getComponent() {
		return layout;
	}

	public void reload() throws HsarwebException {
		loadTable();
	}

	private void initLayout() {
		layout = new VerticalLayout();
		layout.setSizeFull();
		final ModuleConfig moduleConfig = getModuleConfig();
		final LocaleConfig localeConfig = application.getLocaleConfig();
		if (moduleConfig.isSearchAble() || moduleConfig.isAddAble()) {
			HorizontalLayout toolbar = new HorizontalLayout();
			if (moduleConfig.isAddAble()) {
				Button btNew = new Button(moduleConfig.getLabel("new"));
				ThemeResource icon = new ThemeResource("../runo/icons/16/document-add.png");
				btNew.setIcon(icon);
				btNew.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					private Window childWindow;
					@Override
					public void buttonClick(ClickEvent event) {
						final Form form = createForm();
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
									insertRow(form);
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
//			if (moduleConfig.isSearchAble()) {
//				Button btSearch = new Button("search");
//				toolbar.addComponent(btSearch);
//			}
			layout.addComponent(toolbar);
		}
		layout.addComponent(table);
		layout.setExpandRatio(table, 1.0f);
		layout.addComponent(new Link(localeConfig.getText("impressum.label"), new ExternalResource(localeConfig.getText("impressum.link"))));
	}

	private void initTable() throws HsarwebException {
		table = new Table() {
			private static final long serialVersionUID = 35127658139420917L;
			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property property) {
				if (Date.class == property.getType()) {
					try {
						return df.format(property.getValue());
					} catch (IllegalArgumentException e) {
						return "---";
					}
				}
				return super.formatPropertyValue(rowId, colId, property);
			}
		};
		try {
			table.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
			table.setHeight(100.0f, Sizeable.UNITS_PERCENTAGE);
			table.setSelectable(false);
			table.setImmediate(true);
			table.setColumnCollapsingAllowed(true);
			table.setColumnReorderingAllowed(true);
			for (PropertyConfig prop : getModuleConfig().getPropertyList()) {
				PropertyTableColumn propTableColumn = prop.getPropTableColumn();
				if (propTableColumn != PropertyTableColumn.NONE) {
					table.addContainerProperty(prop.getId(), prop.getType(), prop.getDefaultValue());
					table.setColumnHeader(prop.getId(), prop.getLabel());
					if (propTableColumn == PropertyTableColumn.HIDDEN) { 
						table.setColumnCollapsed(prop.getId(), true);
					}
					if (propTableColumn == PropertyTableColumn.INTERNAL_KEY) { 
						table.setColumnCollapsed(prop.getId(), true);
					}
				}
			}
			if (getModuleConfig().isUpdateAble()) {
				table.addContainerProperty("edit", Button.class, null);
				table.setColumnWidth("edit", 16);
				table.setColumnHeader("edit", "");
			}
			if (getModuleConfig().isDeleteAble()) {
				table.addContainerProperty("del", Button.class, null);
				table.setColumnWidth("del", 16);
				table.setColumnHeader("del", "");
			}
		} catch (Exception e) {
			throw new HsarwebException(e);
		}
	}

	private void loadTable() throws HsarwebException {
		table.removeAllItems();
		try {
			ModuleConfig moduleConfig = getModuleConfig();
			Object callSearch = application.getRemote().callSearch(moduleConfig.getName(), new HashMap<String, String>());
			List<PropertyConfig> propertyList = moduleConfig.getPropertyList();
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					long oid = -1L;
					if (row instanceof Map<?, ?>) {
						int numOfcolumns = propertyList.size();
						if (moduleConfig.isUpdateAble()) {
							numOfcolumns++;
						}
						if (moduleConfig.isDeleteAble()) {
							numOfcolumns++;
						}
						numOfcolumns = moduleConfig.getNumOfColumns();
						Object[] itemData = new Object[numOfcolumns];
						int idx = 0;
						for (PropertyConfig prop : propertyList) {
							PropertyTableColumn propTableColumn = prop.getPropTableColumn();
							if (propTableColumn != PropertyTableColumn.NONE) {
								Object valueObject = ((Map<?, ?>) row).get(prop.getId());
								if (valueObject != null && valueObject instanceof String) {
									if (Long.class.equals(prop.getType())) {
										itemData[idx] = Long.parseLong((String) valueObject);
									}
									if (Date.class.equals(prop.getType())) {
										try {
											itemData[idx] = df.parse((String) valueObject);
										} catch (ParseException e) {
											Calendar cal = Calendar.getInstance();
											cal.clear();
											itemData[idx] = cal.getTime();
										}
									}
									if (String.class.equals(prop.getType())) {
										itemData[idx] = (String) valueObject;
									}
									if (propTableColumn == PropertyTableColumn.INTERNAL_KEY && Long.class.equals(prop.getType())) {
										if (valueObject instanceof String) {
											oid = Long.parseLong((String) valueObject);
										}
									}
								}
								idx++;
							}
						}
						if (moduleConfig.isUpdateAble()) {
							itemData[idx] = createEditButton(oid);
							idx++;
						}
						if (moduleConfig.isDeleteAble()) {
							itemData[idx] = createDeleteButton(oid);
							idx++;
						}
						table.addItem(itemData, oid);
					}
				}
				table.sort();
			}
		} catch (UnsupportedOperationException e) {
			throw new HsarwebException(e);
		}
	}

	private void deleteRow(long id) throws HsarwebException {
		Map<String, String> paramHash = new HashMap<String, String>();
		paramHash.put(findIdKey(), Long.toString(id));
		application.getRemote().callDelete(getModuleConfig().getName(), paramHash);
		loadTable();
	}
	
	private void updateRow(Form form) throws HsarwebException {
		Map<String, String> whereHash = new HashMap<String, String>();
		whereHash.put(findIdKey(), ((Long) form.getData()).toString());
		Map<String, String> setHash = new HashMap<String, String>();
		Iterator<Component> componentIterator = form.getLayout().getComponentIterator();
		while (componentIterator.hasNext()) {
			Component c = componentIterator.next();
			transferToHash(setHash, c);
		}
		application.getRemote().callUpdate(getModuleConfig().getName(), setHash, whereHash);
		loadTable();
	}

	private void insertRow(Form form) throws HsarwebException {
		Map<String, String> setHash = new HashMap<String, String>();
		Iterator<Component> componentIterator = form.getLayout().getComponentIterator();
		while (componentIterator.hasNext()) {
			Component c = componentIterator.next();
			transferToHash(setHash, c);
		}
		application.getRemote().callAdd(getModuleConfig().getName(), setHash);
		loadTable();
	}

	private void transferToHash(Map<String, String> setHash, Component c) {
		if (c instanceof AbstractTextField) {
			AbstractTextField tf = (AbstractTextField) c;
			Object data = tf.getData();
			Object value = tf.getValue();
			setHash.put((String) data, (String) value);
		}
		if (c instanceof Select) {
			Select sel = (Select) c; 
			Object data = sel.getData();
			Object value = sel.getValue();
			setHash.put((String) data, (String) value);
		}
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
	
	private Button createEditButton(long id) {
		ThemeResource icon = new ThemeResource("../runo/icons/16/document-txt.png");
		final Button button = new Button();
		button.setIcon(icon);
		button.setData(id);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			private Window childWindow;
			@Override
			public void buttonClick(ClickEvent event) {
				final Form form = createForm((Long) button.getData());
				LocaleConfig localeConfig = application.getLocaleConfig();
				childWindow = new Window(getModuleConfig().getLabel("moduletitle") + " " + localeConfig.getText("update"));
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
							updateRow(form);
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
		return button;
	}

	private Button createDeleteButton(long id) {
		ThemeResource icon = new ThemeResource("../runo/icons/16/document-delete.png");
		final Button button = new Button();
		button.setIcon(icon);
		button.setData(id);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			private Window childWindow;
			@Override
			public void buttonClick(ClickEvent event) {
				LocaleConfig localeConfig = application.getLocaleConfig();
				childWindow = new Window(getModuleConfig().getLabel("moduletitle") + " " + localeConfig.getText("delete"));
				childWindow.setWidth(320.0f, Sizeable.UNITS_PIXELS);
				VerticalLayout vLayout = new VerticalLayout();
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				vLayout.addComponent(new Label(localeConfig.getText("confirmdelete")));
				HorizontalLayout hLayout = new HorizontalLayout();
				Button btDeleteRow = new Button(localeConfig.getText("delete"));
				btDeleteRow.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						application.getMainWindow().removeWindow(childWindow);
						try {
							deleteRow((Long) button.getData());
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
				hLayout.addComponent(btDeleteRow);
				hLayout.addComponent(btAbort);
				vLayout.addComponent(hLayout);
				childWindow.setContent(vLayout);
				childWindow.setModal(true);
				application.getMainWindow().addWindow(childWindow);
			}
		});
		return button;
	}

	private Form createForm(Long key) {
		try {
			Map<String, String> where = new HashMap<String, String>();
			where.put(findIdKey(), key.toString());
			Object searchResult = application.getRemote().callSearch(getModuleConfig().getName(), where);
			if (searchResult instanceof Object[]) {
				Map<?, ?> row = (Map<?, ?>) (((Object[]) searchResult)[0]);
				Form f = new Form();
				f.setCaption(getModuleConfig().getLabel("update"));
				f.setData(key);
				Layout layout = f.getLayout();
				for (PropertyConfig prop : getModuleConfig().getPropertyList()) {
					PropertyFormField propFormField = prop.getPropFormField();
					if (propFormField != PropertyFormField.INTERNAL_KEY && propFormField != PropertyFormField.NONE) {
						if (prop.hasSelectList()) {
							Select sel = new Select(prop.getLabel());
							sel.setData(prop.getId());
							sel.setNullSelectionAllowed(false);
							sel.setNewItemsAllowed(prop.newItemsAllowed());
							Map<String, String> selectValues = prop.getSelectValues();
							for (Object skey : selectValues.keySet()) {
								sel.addItem(skey);
								sel.setItemCaption(skey, selectValues.get(skey));
							}
							sel.setWidth(480.0f, Sizeable.UNITS_PIXELS);
							Object value = row.get(prop.getId());
							sel.setValue(value != null ? value : prop.getDefaultValue());
							sel.setReadOnly(PropertyFormField.READONLY == propFormField || PropertyFormField.WRITEONCE == propFormField);
							layout.addComponent(sel);
						} else {
							if (propFormField != PropertyFormField.PASSWORD) {
								TextField tf = new TextField(prop.getLabel());
								tf.setData(prop.getId());
								tf.setWidth(480.0f, Sizeable.UNITS_PIXELS);
								Object value = row.get(prop.getId());
								tf.setValue(value != null ? value : prop.getDefaultValue());
								tf.setReadOnly(PropertyFormField.READONLY == propFormField || PropertyFormField.WRITEONCE == propFormField);
								layout.addComponent(tf);
							}
						}
					}
				}
				return f;
			}
		} catch (HsarwebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Form createForm() {
		Form f = new Form();
		f.setCaption(getModuleConfig().getLabel("new"));
		Layout layout = f.getLayout();
		for (PropertyConfig prop : getModuleConfig().getPropertyList()) {
			PropertyFormField propFormField = prop.getPropFormField();
			if (PropertyFormField.READWRITE == propFormField || PropertyFormField.WRITEONCE == propFormField || PropertyFormField.PASSWORD == propFormField) {
				if (prop.hasSelectList()) {
					Select sel = new Select(prop.getLabel());
					sel.setData(prop.getId());
					sel.setNullSelectionAllowed(false);
					sel.setNewItemsAllowed(prop.newItemsAllowed());
					Map<String, String> selectValues = prop.getSelectValues();
					for (Object key : selectValues.keySet()) {
						sel.addItem(key);
						sel.setItemCaption(key, selectValues.get(key));
					}
					sel.setWidth(480.0f, Sizeable.UNITS_PIXELS);
					sel.setValue(prop.getDefaultValue());
					layout.addComponent(sel);
				} else {
					if (PropertyFormField.PASSWORD == propFormField) {
						PasswordField tf1 = new PasswordField(getModuleConfig().getLabel(prop.getId() + "1"));
						tf1.setData(prop.getId());
						tf1.setWidth(480.0f, Sizeable.UNITS_PIXELS);
						tf1.setValue(prop.getDefaultValue());
						layout.addComponent(tf1);
						PasswordField tf2 = new PasswordField(getModuleConfig().getLabel(prop.getId() + "2"));
						tf2.setData(prop.getId());
						tf2.setWidth(480.0f, Sizeable.UNITS_PIXELS);
						tf2.setValue(prop.getDefaultValue());
						layout.addComponent(tf2);
					} else {
						TextField tf = new TextField(prop.getLabel());
						tf.setData(prop.getId());
						tf.setWidth(480.0f, Sizeable.UNITS_PIXELS);
						tf.setValue(prop.getDefaultValue());
						layout.addComponent(tf);
					}
				}
			}
		}
		return f;
	}
	
}
