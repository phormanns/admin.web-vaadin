package de.hsadmin.web;

public class PanelFactory extends AbstractFactory{

	@Override
	IHSPanel getPanel(String panelType) {

		if(panelType == null)
			return null;
		
		if(panelType.equalsIgnoreCase("MANAGED SERVERS"))
			return new ServerPanel();
		
		if(panelType.equalsIgnoreCase("DOMAINS"))
			return new DomainPanel();
		
		if(panelType.equalsIgnoreCase("PACKAGE"))
			return new PackagePanel();
		
		return null;
	}

	@Override
	IHSWindow getSubWindow(String type, String subType) {
		return null;
	}

}
