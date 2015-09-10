package de.hsadmin.web;

public class FactoryProducer {

	public static AbstractPanelFactory getPanelFactory(String choice) 
	{
		return new PanelFactory();
	}

	public static AbstractWindowFactory getWindowFactory(String choice) 
	{
		return new SubWindowFactory();
	}

	
}
