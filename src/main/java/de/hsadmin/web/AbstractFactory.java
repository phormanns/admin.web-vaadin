package de.hsadmin.web;

public abstract class AbstractFactory {

	public abstract IHSPanel getPanel(String panelType);
	
	public abstract IHSWindow getSubWindow(String type, String action);

}
