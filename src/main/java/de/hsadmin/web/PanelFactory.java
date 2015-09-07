package de.hsadmin.web;

public class PanelFactory extends AbstractFactory {

	@Override
	public IHSPanel getPanel(String panelType) {

		if (panelType == null)
			return null;

		if (panelType.equals("hive"))
			return new ServerPanel();

		if (panelType.equals("domain"))
			return new DomainPanel();

		if (panelType.equals("pac"))
			return new PackagePanel();

		return null;
	}

	@Override
	public IHSWindow getSubWindow(String type, String subType) {
		return null;
	}

}
