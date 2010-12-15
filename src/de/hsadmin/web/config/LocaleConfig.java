package de.hsadmin.web.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleConfig {

	private ResourceBundle bundle;

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
