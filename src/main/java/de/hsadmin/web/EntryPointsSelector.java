package de.hsadmin.web;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;

public class EntryPointsSelector extends CustomComponent implements ItemClickListener, SelectedTabChangeListener {

	private static final long serialVersionUID = 1L;
	
	private final MainWindow mainWindow;
	
	private Accordion accordion;

	public EntryPointsSelector(final MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		final Panel panel = new Panel();
		accordion = new Accordion();
		accordion.setSizeFull();
//		content.setHeight(100.0f, Unit.PERCENTAGE);
		createTabs();
		panel.setContent(accordion);
		panel.setSizeFull();
		setCompositionRoot(panel);
		accordion.addSelectedTabChangeListener(this);
	}

	private void createTabs() {
		for(String tabName : new String[] { "customer", "pac", "domain" }){
			accordion.addTab(new EntryPoint(this, tabName), tabName);
		}
		final Component component = accordion.getTab(0).getComponent();
		if (component instanceof EntryPoint) {
			((EntryPoint) component).fillTable();
		}
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		Table table = (Table) event.getSource();
		mainWindow.setCenterPanel((String) table.getData(), event.getItemId());
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		final Component selectedTab = event.getTabSheet().getSelectedTab();
		if (selectedTab instanceof EntryPoint) {
			((EntryPoint) selectedTab).fillTable();
		}
	}
	
	public MainWindow getMainWindow() {
		return mainWindow;
	}
}