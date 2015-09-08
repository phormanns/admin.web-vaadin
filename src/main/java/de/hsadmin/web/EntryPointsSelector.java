package de.hsadmin.web;

import java.util.List;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;

public class EntryPointsSelector extends CustomComponent implements ItemClickListener{

	private static final long serialVersionUID = 1L;
	
	private final MainWindow mainWindow;
	
	private Accordion content;

	public EntryPointsSelector(final MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		final Panel panel = new Panel();
		content = new Accordion();

		createTabs();

		panel.setContent(content);

		setCompositionRoot(panel);
	}

	private void createTabs() {
		for(String tabName : new String[] { "pac", "domain" }){
			content.addTab(getLeftComponent(tabName));
		}
	}

	private Table getLeftComponent(String name){
		Table leftComponent = new Table(name);
		leftComponent.setData(name);

		final String[] entryPointColumns = mainWindow.entryPointColumns(name);
		
		if (entryPointColumns != null && entryPointColumns.length > 0) {
			for (String col : entryPointColumns) {
				leftComponent.addContainerProperty(col, String.class, null);
			}
			final List<Object[]> list = mainWindow.list(name, entryPointColumns);
			for (Object[] row : list) {
				leftComponent.addItem(row, row[0]);
			}
		}
		leftComponent.setPageLength(leftComponent.size());
		leftComponent.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		leftComponent.addItemClickListener(this);
		leftComponent.setSelectable(true);
		leftComponent.setImmediate(true);
		leftComponent.setSizeFull();

		return leftComponent;
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		Table table = (Table) event.getSource();
		mainWindow.setCenterPanel((String) table.getData(), event.getItemId());
	}
}