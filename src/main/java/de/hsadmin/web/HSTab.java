package main.java.de.hsadmin.web;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class HSTab extends VerticalLayout{
	
	private static final long serialVersionUID = 8755003327696243905L;
	private HorizontalLayout panelToolbar;
	private Table grid;
	
	public HSTab(){
		super();
		panelToolbar = new PanelToolbar();
		
		addComponent(panelToolbar);
		setComponentAlignment(panelToolbar, Alignment.MIDDLE_RIGHT);
		addComponent(getGrid());
	}

	private Table getGrid() {
		grid = new Table();

		grid.addContainerProperty("Id", String.class, null);
		grid.addContainerProperty("Description", String.class, null);

		grid.addItem(new Object[]{"AAA", "BBB"}, 1);
		grid.addItem(new Object[]{"AAAA", "BBBB"}, 2);
		grid.addItem(new Object[]{"CCCC", "DDDD"}, 3);

		grid.setPageLength(grid.size());
		//pkgList.addItemClickListener(this);
		grid.setSelectable(true);
		grid.setImmediate(true);
		grid.setSizeFull();

		return grid;
	}

}
