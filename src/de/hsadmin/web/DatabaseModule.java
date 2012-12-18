package de.hsadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertySelectValues;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.SelectPropertyFieldFactory;

public abstract class DatabaseModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public abstract String getModuleIdent();

	public abstract String getUserModuleIdent();

	public abstract String[] getEncodings();

	@Override
	protected void initModule() {
		MainApplication application = getApplication();
		moduleConfig = new ModuleConfig(getModuleIdent(), application.getLocale());
		String login = application.getRunAs();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
		PropertyConfig nameProp = new PropertyConfig(moduleConfig, "name", String.class);
		nameProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				if (pac.length() >= 5) {
					return pac + "_";
				}
				return "";
			}
		});
		nameProp.setWriteOnce(true);
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN, new SelectPropertyFieldFactory());
		pacProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac;
			}
		});
		pacProp.setSelectValues(new PropertySelectValues() {
			@Override
			public boolean newItemsAllowed() {
				return false;
			}
			@Override
			public boolean hasSelectList() {
				return true;
			}
			@Override
			public Map<String, String> getSelectValues() {
				List<String> list = getPackets();
				TreeMap<String,String> map = new TreeMap<String, String>();
				for (String pac : list) {
					map.put(pac, pac);
				}
				return map;
			}
		});
		pacProp.setWriteOnce(true);
		PropertyConfig encodingProp = new PropertyConfig(moduleConfig, "encoding", String.class, new SelectPropertyFieldFactory());
		encodingProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return "utf8";
			}
		});
		encodingProp.setSelectValues(new PropertySelectValues() {
			@Override
			public boolean newItemsAllowed() {
				return false;
			}
			@Override
			public Map<String, String> getSelectValues() {
				String[] encodings = getEncodings();
				Map<String,String> map = new TreeMap<String, String>();
				for (String enc : encodings) {
					map.put(enc, enc);
				}
				return map;
			}
			@Override
			public boolean hasSelectList() {
				return true;
			}
		});
		encodingProp.setWriteOnce(true);
		PropertyConfig ownerProp = new PropertyConfig(moduleConfig, "owner", String.class, new SelectPropertyFieldFactory());
		ownerProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return "";
			}
		});
		ownerProp.setSelectValues(new PropertySelectValues() {
			@Override
			public boolean newItemsAllowed() {
				return false;
			}
			@Override
			public boolean hasSelectList() {
				return true;
			}
			@Override
			public Map<String, String> getSelectValues() {
				List<String> list = getDatabaseUsers();
				TreeMap<String,String> map = new TreeMap<String, String>();
				for (String usr : list) {
					map.put(usr, usr);
				}
				return map;
			}
		});
		idProp.setShowInForm(false);
		pacProp.setShowInForm(false);
		encodingProp.setShowInForm(false);
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(pacProp);
		moduleConfig.addProperty(nameProp);
		moduleConfig.addProperty(encodingProp);
		moduleConfig.addProperty(ownerProp);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

	public List<String> getDatabaseUsers() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Object callSearch = getApplication().getRemote().callSearch(getUserModuleIdent(), new HashMap<String, AbstractProperty>());
			if (callSearch instanceof Object[]) {
				for (Object row : ((Object[])callSearch)) {
					if (row instanceof Map<?, ?>) {
						Object object = ((Map<?, ?>) row).get("name");
						if (object instanceof String) {
							list.add((String) object);
						}
					}
				}
			}
		} catch (HsarwebException e) {
			e.printStackTrace();
			getApplication().showSystemException(e);
		}
		return list;
	}

}
