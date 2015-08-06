package de.hsadmin.web;

public abstract class AbstractFactory {

	abstract IHSPanel getPanel(String panelType);
	abstract IHSWindow getSubWindow(String type, String action);

}
