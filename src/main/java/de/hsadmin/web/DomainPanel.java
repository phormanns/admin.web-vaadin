package main.java.de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

public class DomainPanel extends CustomComponent implements IHSPanel{

	private static final long serialVersionUID = 2223638269308264340L;

	public DomainPanel() {

		final Panel panel  = new Panel();
		
		TabSheet tabsheet = createTabs();
        panel.setContent(tabsheet);

		setCompositionRoot(panel);
	}

	@Override
	public TabSheet createTabs() {

		TabSheet tabsheet = new TabSheet();
		tabsheet.addTab(new HSTab(), "Email Address");

		return tabsheet;
	}
}