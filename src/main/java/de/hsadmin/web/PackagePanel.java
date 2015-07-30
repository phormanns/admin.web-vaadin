package main.java.de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

public class PackagePanel extends CustomComponent implements IHSPanel{

	private static final long serialVersionUID = 5397852733862398775L;

	public PackagePanel() {

		final Panel panel  = new Panel();
		
		TabSheet tabsheet = createTabs();
        panel.setContent(tabsheet);

		setCompositionRoot(panel);
		setSizeFull();
	}

	@Override
	public TabSheet createTabs() {

		TabSheet tabsheet = new TabSheet();
		tabsheet.addTab(new HSTab(), "User");
		tabsheet.addTab(new HSTab(), "Data Base");
		tabsheet.addTab(new HSTab(), "Aliases");

		return tabsheet;
	}

}
