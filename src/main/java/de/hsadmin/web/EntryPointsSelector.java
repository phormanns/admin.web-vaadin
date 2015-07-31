package main.java.de.hsadmin.web;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class EntryPointsSelector extends CustomComponent implements ItemClickListener{

	private static final long serialVersionUID = 1L;
	private CustomizationPanel custom = CustomizationPanel.getInstance();
	private Accordion content;

	public EntryPointsSelector() {
		final Panel panel = new Panel("Entry Point");
		content = new Accordion();

		createTabs();

		panel.setContent(content);

		setCompositionRoot(panel);
	}

	private void createTabs() {
		for(String tabName : custom.getSelectedOptions()){
			content.addTab(getLeftComponent(tabName));
		}
	}

	public Table getLeftComponent(String name){
		Table leftComponent = new Table(name);

		leftComponent.addContainerProperty("Id", String.class, null);
		leftComponent.addContainerProperty("Description", String.class, null);

		leftComponent.addItem(new Object[]{"AAA", "BBB"}, 1);
		leftComponent.addItem(new Object[]{"AAAA", "BBBB"}, 2);
		leftComponent.addItem(new Object[]{"CCCC", "DDDD"}, 3);

		leftComponent.setPageLength(leftComponent.size());
		leftComponent.addItemClickListener(this);
		leftComponent.setSelectable(true);
		leftComponent.setImmediate(true);
		leftComponent.setSizeFull();

		return leftComponent;
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		Table table = (Table) event.getSource();
		MainWindow.setCenterPanel(table.getCaption());
	}
}