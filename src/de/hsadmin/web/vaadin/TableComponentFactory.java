package de.hsadmin.web.vaadin;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

import de.hsadmin.web.DeleteAble;
import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.MainApplication;
import de.hsadmin.web.Module;
import de.hsadmin.web.UpdateAble;
import de.hsadmin.web.config.ComponentFactory;
import de.hsadmin.web.config.LocaleConfig;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyTableColumn;

public class TableComponentFactory implements ComponentFactory, Serializable {

	private static final long serialVersionUID = 1L;
	private static final DateFormat serverDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

	private Module module;
	private Table table;
	private DateFormat browserDateFormat;

	public TableComponentFactory(Module module) {
		this.module = module;
		this.browserDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, module.getApplication().getLocale());
	}
	
	@Override
	public Object initComponent() throws HsarwebException {
		ModuleConfig config = module.getModuleConfig();
		table = new Table() {
			private static final long serialVersionUID = 1L;
			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property property) {
				if (Date.class == property.getType()) {
					try {
						return browserDateFormat.format(property.getValue());
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
			table.setColumnReorderingAllowed(false);
			for (PropertyConfig prop : config.getPropertyList()) {
				PropertyTableColumn propTableColumn = prop.getPropTableColumn();
				if (propTableColumn != PropertyTableColumn.NONE) {
					table.addContainerProperty(prop.getId(), prop.getType(), prop.getDefaultValue());
					table.setColumnExpandRatio(prop.getId(), prop.getExpandRatio());
					table.setColumnHeader(prop.getId(), prop.getLabel());
					if (propTableColumn == PropertyTableColumn.HIDDEN) { 
						table.setColumnCollapsed(prop.getId(), true);
					}
					if (propTableColumn == PropertyTableColumn.INTERNAL_KEY) { 
						table.setColumnCollapsed(prop.getId(), true);
					}
				}
			}
			if (module instanceof UpdateAble) {
				table.addContainerProperty("edit", Button.class, null);
				table.setColumnWidth("edit", 16);
				table.setColumnHeader("edit", "");
			}
			if (module instanceof DeleteAble) {
				table.addContainerProperty("del", Button.class, null);
				table.setColumnWidth("del", 16);
				table.setColumnHeader("del", "");
			}
		} catch (Exception e) {
			throw new HsarwebException(e);
		}
		return table;
	}

	@Override
	public void loadData() throws HsarwebException {
		table.removeAllItems();
		try {
			ModuleConfig moduleConfig = module.getModuleConfig();
			Object callSearch = module.getApplication().getRemote().callSearch(moduleConfig.getRemoteName(), new HashMap<String, String>());
			List<PropertyConfig> propertyList = moduleConfig.getPropertyList();
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					long oid = -1L;
					if (row instanceof Map<?, ?>) {
						int numOfcolumns = moduleConfig.getNumOfColumns();
						if (module instanceof UpdateAble) {
							numOfcolumns++;
						}
						if (module instanceof DeleteAble) {
							numOfcolumns++;
						}
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
											itemData[idx] = serverDateFormat.parse((String) valueObject);
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
						if (module instanceof UpdateAble) {
							itemData[idx] = createEditButton(oid);
							idx++;
						}
						if (module instanceof DeleteAble) {
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

	private Button createEditButton(long id) {
		ThemeResource icon = new ThemeResource("../runo/icons/16/document-txt.png");
		final Button button = new Button();
		final Module thisModule = module;
		button.setIcon(icon);
		button.setDescription(module.getApplication().getLocaleConfig().getText("update"));
		button.setData(id);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			private Window childWindow;
			@Override
			public void buttonClick(ClickEvent event) {
				final GenericForm genericForm = new GenericForm(thisModule, (Long) button.getData());
				final Form form = genericForm.createUpdateForm();
				final MainApplication application = module.getApplication();
				LocaleConfig localeConfig = application.getLocaleConfig();
				childWindow = new Window(module.getModuleConfig().getLabel("moduletitle") + " " + localeConfig.getText("update"));
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
							((UpdateAble) module).updateRow(map);
							loadData();
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
		button.setDescription(module.getApplication().getLocaleConfig().getText("delete"));
		button.setData(id);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			private Window childWindow;
			@Override
			public void buttonClick(ClickEvent event) {
				final MainApplication application = module.getApplication();
				LocaleConfig localeConfig = application.getLocaleConfig();
				childWindow = new Window(module.getModuleConfig().getLabel("moduletitle") + " " + localeConfig.getText("delete"));
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
							Map<String, String> map = new HashMap<String, String>();
							map.put(findIdKey(), ((Long) button.getData()).toString());
							((DeleteAble) module).deleteRow(map);
							loadData();
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

	private String findIdKey() {
		List<PropertyConfig> propertyList = module.getModuleConfig().getPropertyList();
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
	

}
