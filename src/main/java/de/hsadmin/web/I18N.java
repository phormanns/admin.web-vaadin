package de.hsadmin.web;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18N {

	private static final ResourceBundle TEXTS = ResourceBundle.getBundle("de.hsadmin.web.main");
	
	public static String getText(final String textProperty) {
		String textValue;
		try{
			textValue = I18N.TEXTS.getString(textProperty);
		}catch(MissingResourceException e){
			textValue = "./. " + textProperty;
		}
		return textValue;
	}

}
