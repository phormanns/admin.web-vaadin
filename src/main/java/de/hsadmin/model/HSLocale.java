package de.hsadmin.model;

import java.util.Locale;

public class HSLocale {
	
	private static HSLocale singleton;
	private Locale chosenLocale;
	
	private HSLocale(){
	}
	
	public static HSLocale getHSLocale(){
		if(singleton == null){
			singleton = new HSLocale();
		}
		return singleton;
	}
	
	public void setLocale(String language){
		chosenLocale = new Locale(language);
	}
	
	public Locale getLocale(){
		return chosenLocale;
	}
}
