package de.hsadmin.web;

import java.util.List;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.VerticalLayout;

public class EntryPoint extends CustomComponent {

	private static final long serialVersionUID = 1L;

	final private String module;
	final private EntryPointsSelector parent;
	final private Table selectTable;
	
	public EntryPoint(EntryPointsSelector parent, String name) {
		this.module = name;
		this.parent = parent;
		setCaption(name);
		final VerticalLayout tab = new VerticalLayout();
		selectTable = new Table();
		selectTable.setData(name);
		final MainWindow mainWindow = parent.getMainWindow();
		final String[] entryPointColumns = mainWindow.entryPointColumns(name);
		if (entryPointColumns != null && entryPointColumns.length > 0) {
			for (String col : entryPointColumns) {
				selectTable.addContainerProperty(col, String.class, null);
			}
		}
		selectTable.setPageLength(0);
		selectTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		selectTable.addItemClickListener(parent);
		selectTable.setSelectable(true);
		selectTable.setImmediate(true);
		selectTable.setSizeFull();
		tab.addComponent(selectTable);
		tab.setSizeFull();
		setCompositionRoot(tab);
	}

	public void fillTable() {
		selectTable.removeAllItems();
		final MainWindow mainWindow = parent.getMainWindow();
		final String[] entryPointColumns = mainWindow.entryPointColumns(module);
		final List<Object[]> list = mainWindow.list(module, entryPointColumns);
		for (Object[] row : list) {
			selectTable.addItem(row, row[0]);
		}
		selectTable.setSortContainerPropertyId(entryPointColumns[0]);
		selectTable.sort();
		selectTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		selectTable.setSelectable(true);
		selectTable.setImmediate(true);
		selectTable.setSizeFull();
	}

}
