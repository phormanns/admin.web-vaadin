package de.hsadmin.web;


public class PanelFactory extends AbstractFactory{

	@Override
	IHSPanel getPanel(String panelType) {

		if(panelType == null)
			return null;
		
		if(panelType.equals("hive"))
			return new ServerPanel();
		
		if(panelType.equalsIgnoreCase("DOMAINS"))
			return new DomainPanel();
		
		if(panelType.equals("pac"))
			return new PackagePanel();
		
		return null;
	}

	@Override
	IHSWindow getSubWindow(String type, String subType) {
		return null;
	}

}
