package de.hsadmin.web;

import java.util.Map;
import java.util.TreeMap;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyDefaultValue;
import de.hsadmin.web.config.PropertySelectValues;
import de.hsadmin.web.config.PropertyTableColumn;
import de.hsadmin.web.vaadin.PasswordPropertyFieldFactory;
import de.hsadmin.web.vaadin.SelectPropertyFieldFactory;

public class UnixUserModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	@Override
	protected void initModule() {
		moduleConfig = new ModuleConfig("user");
		String login = getApplication().getLogin();
		final String pac = login.length() >= 5 ? login.substring(0, 5) : "";
		PropertyConfig idProp = new PropertyConfig(moduleConfig, "id", Long.class, PropertyTableColumn.INTERNAL_KEY);
		idProp.setReadOnly(true);
		PropertyConfig useridProp = new PropertyConfig(moduleConfig, "userid", Long.class, PropertyTableColumn.HIDDEN);
		useridProp.setReadOnly(true);
		PropertyConfig nameProp = new PropertyConfig(moduleConfig, "name", String.class);
		nameProp.setWriteOnce(true);
		nameProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				if (pac.length() > 0) {
					return pac + "-";
				}
				return "";
			}
		});
		PropertyConfig passwordProp = new PropertyConfig(moduleConfig, "password", String.class, PropertyTableColumn.NONE, new PasswordPropertyFieldFactory(this));
		PropertyConfig commentProp = new PropertyConfig(moduleConfig, "comment", String.class);
		PropertyConfig shellProp = new PropertyConfig(moduleConfig, "shell", String.class, new SelectPropertyFieldFactory());
		shellProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return "/usr/bin/passwd";
			}
		});
		shellProp.setSelectValues(new PropertySelectValues() {
			@Override
			public boolean newItemsAllowed() {
				return false;
			}
			@Override
			public Map<String, String> getSelectValues() {
				Map<String,String> map = new TreeMap<String, String>();
				map.put("/usr/bin/passwd", "/usr/bin/passwd");
				map.put("/bin/bash", "/bin/bash");
				map.put("/bin/dash", "/bin/dash");
				map.put("/bin/csh", "/bin/csh");
				map.put("/bin/tcsh", "/bin/tcsh");
				map.put("/bin/ksh", "/bin/ksh");
				map.put("/bin/zsh", "/bin/zsh");
				map.put("/usr/bin/scponly", "/usr/bin/scponly");
				return map;
			}
			@Override
			public boolean hasSelectList() {
				return true;
			}
		});
		PropertyConfig homedirProp = new PropertyConfig(moduleConfig, "homedir", String.class, PropertyTableColumn.HIDDEN);
		homedirProp.setReadOnly(true);
		PropertyConfig pacProp = new PropertyConfig(moduleConfig, "pac", String.class, PropertyTableColumn.HIDDEN);
		pacProp.setReadOnly(true);
		pacProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return pac;
			}
		});
		PropertyConfig softQuotaProp = new PropertyConfig(moduleConfig, "quota_softlimit", Long.class, PropertyTableColumn.HIDDEN);
		softQuotaProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return "0";
			}
		});
		PropertyConfig hardQuotaProp = new PropertyConfig(moduleConfig, "quota_hardlimit", Long.class, PropertyTableColumn.HIDDEN);
		hardQuotaProp.setDefaultValue(new PropertyDefaultValue() {
			@Override
			public String getDefaultValue() {
				return "0";
			}
		});
		moduleConfig.addProperty(idProp);
		moduleConfig.addProperty(useridProp);
		moduleConfig.addProperty(nameProp);
		moduleConfig.addProperty(passwordProp);
		moduleConfig.addProperty(commentProp);
		moduleConfig.addProperty(shellProp);
		moduleConfig.addProperty(homedirProp);
		moduleConfig.addProperty(pacProp);
		moduleConfig.addProperty(softQuotaProp);
		moduleConfig.addProperty(hardQuotaProp);
	}

	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
