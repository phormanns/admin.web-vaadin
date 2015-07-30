package main.java.de.hsadmin.web;

public class PanelFactory extends AbstractPanelFactory{

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

}
