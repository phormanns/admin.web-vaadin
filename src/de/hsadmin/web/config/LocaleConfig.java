package de.hsadmin.web.config;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ResourceBundle bundle;

	public LocaleConfig(Locale locale, String moduleName) {
		bundle = ResourceBundle.getBundle("texts." + moduleName, locale);
	}
	
	public String getText(String key) {
		try {
			return bundle.getString(key);
		} catch (Exception e) {
			return "!!" + key + "!!";
		}
	}
}
