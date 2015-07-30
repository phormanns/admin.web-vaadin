package main.java.de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

public class ServerPanel extends CustomComponent implements IHSPanel{

	private static final long serialVersionUID = 5720017114380927259L;
	
	public ServerPanel() {

		final Panel panel  = new Panel();
		
		TabSheet tabsheet = createTabs();
		tabsheet.setSizeFull();
        panel.setContent(tabsheet);

		setCompositionRoot(panel);
		setSizeFull();
	}

	@Override
	public TabSheet createTabs() {

		TabSheet tabsheet = new TabSheet();
		tabsheet.addTab(new HSTab(), "Packages");
		tabsheet.addTab(new HSTab(), "Network");
		
		return tabsheet;
	}

}
