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

	public static IEditorFactory getEditorFactory(String choice) 
	{
		return new GenericEditorFactory();
	}

	public static AbstractEntryPointsFactory getEntryPointsFactory(String choice) {
		return new EntryPointsFactory();
	}
	
}
