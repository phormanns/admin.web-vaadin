package de.hsadmin.web;

public class FactoryProducer {
	
	public static AbstractFactory getFactory(String choice){
		
		if(choice.equalsIgnoreCase("panel")){
			return new PanelFactory();
		}
		if(choice.equalsIgnoreCase("subwindow")){
			return new SubWindowFactory();
		}
		
		return null;
	}
}
