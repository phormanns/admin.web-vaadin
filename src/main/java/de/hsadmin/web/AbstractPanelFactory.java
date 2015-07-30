package main.java.de.hsadmin.web;

public abstract class AbstractPanelFactory {
	abstract IHSPanel getPanel(String panelType);
}
