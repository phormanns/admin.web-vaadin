package de.hsadmin.web;

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
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

import de.hsadmin.web.config.LocaleConfig;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;

public abstract class GenericModule implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

	private Table table;
	private MainApplication application;

	public void setApplication(MainApplication app) throws HsarwebException {
		this.application = app;
		initTable();
	}
	
	public abstract ModuleConfig getModuleConfig();

	public Component getComponent() {
		return table;
	}

	public void reload() throws HsarwebException {
		loadTable();
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
			table.setSelectable(true);
			table.setImmediate(true);
			table.setColumnCollapsingAllowed(true);
			table.setColumnReorderingAllowed(true);
			for (PropertyConfig prop : getModuleConfig().getPropertyList()) {
				table.addContainerProperty(prop.getId(), prop.getType(), prop.getDefaultValue());
				table.setColumnHeader(prop.getId(), prop.getLabel());
				if (prop.isHidden()) { 
					table.setColumnCollapsed(prop.getId(), true);
				}
			}
			table.addContainerProperty("edit", Button.class, null);
			table.setColumnWidth("edit", 16);
			table.setColumnHeader("edit", "");
			table.addContainerProperty("del", Button.class, null);
			table.setColumnWidth("del", 16);
			table.setColumnHeader("del", "");
		} catch (Exception e) {
			throw new HsarwebException(e);
		}
	}

	private void loadTable() throws HsarwebException {
		table.removeAllItems();
		try {
			Object callSearch = application.getRemote().callSearch(getModuleConfig().getName(), new HashMap<String, String>());
			List<PropertyConfig> propertyList = getModuleConfig().getPropertyList();
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					long oid = -1L;
					if (row instanceof Map<?, ?>) {
						Object[] itemData = new Object[propertyList.size()+2];
						int idx = 0;
						for (PropertyConfig prop : propertyList) {
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
								if (prop.isIdent() && Long.class.equals(prop.getType())) {
									if (valueObject instanceof String) {
										oid = Long.parseLong((String) valueObject);
									}
								}
							}
							idx++;
						}
						itemData[idx] = createEditButton(oid);
						idx++;
						itemData[idx] = createDeleteButton(oid);
						idx++;
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
	
	private String findIdKey() {
		List<PropertyConfig> propertyList = getModuleConfig().getPropertyList();
		String idKey = null;
		for (PropertyConfig propConf : propertyList) {
			if (propConf.isIdent()) {
				idKey = propConf.getId();
				return idKey;
			}
		}
		return idKey;
	}
	
	private Button createEditButton(long id) {
		ThemeResource icon = new ThemeResource("../runo/icons/16/document-txt.png");
		Button button = new Button();
		button.setIcon(icon);
		button.setData(id);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
//				loadTable();
				System.out.println("Data: " + event.getButton().getData());
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

}
