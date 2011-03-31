package de.hsadmin.web.vaadin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;

import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.MainApplication;
import de.hsadmin.web.Module;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;
import de.hsadmin.web.config.PropertyTableColumn;

public class GenericForm {

	private Module module;
	private Long entityId;
	
	public GenericForm(Module module, Long entityId) {
		this.module = module;
		this.entityId = entityId;
	}
	
	public Form createAddForm() {
		Form f = new Form();
		ModuleConfig config = module.getModuleConfig();
		f.setCaption(config.getLabel("new"));
		Layout layout = f.getLayout();
		for (PropertyConfig prop : config.getPropertyList()) {
			PropertyFieldFactory propFieldFactory = prop.getPropFieldFactory();
			if (!propFieldFactory.isReadOnly()) {
				layout.addComponent((Component) propFieldFactory.createFieldComponent(prop, null));
			}
		}
		return f;
	}
	
	public Form createUpdateForm() {
		try {
			MainApplication application = module.getApplication();
			ModuleConfig config = module.getModuleConfig();
			Map<String, String> where = new HashMap<String, String>();
			where.put(findIdKey(), entityId.toString());
			Object searchResult = application.getRemote().callSearch(config.getRemoteName(), where);
			if (searchResult instanceof Object[]) {
				Map<?, ?> row = (Map<?, ?>) (((Object[]) searchResult)[0]);
				Form f = new Form();
				f.setCaption(config.getLabel("update"));
				f.setData(entityId);
				Layout layout = f.getLayout();
				for (PropertyConfig prop : config.getPropertyList()) {
					if (!prop.getPropTableColumn().equals(PropertyTableColumn.INTERNAL_KEY)) {
						PropertyFieldFactory propFieldFactory = prop.getPropFieldFactory();
						Object value = row.get(prop.getId());
						Component component = (Component) propFieldFactory.createFieldComponent(prop, value);
						if (propFieldFactory.isWriteOnce()) {
							component.setReadOnly(true);
						}
						layout.addComponent(component);
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

	public void transferToHash(Map<String, String> map, Form form) throws HsarwebException {
		Iterator<Component> iterator = form.getLayout().getComponentIterator();
		Object formData = form.getData();
		if (formData != null && formData instanceof Long) {
			map.put(findIdKey(), ((Long) formData).toString());
		}
		while (iterator.hasNext()) {
			Component component = (Component) iterator.next();
			if (component instanceof AbstractComponent) {
				Object data = ((AbstractComponent) component).getData();
				String propName = (String) data;
				PropertyConfig property = module.getModuleConfig().getProperty(propName);
				map.put(propName, property.getPropFieldFactory().getValue(property, component));
			}
		}
	}

}
