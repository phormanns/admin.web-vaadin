package de.hsadmin.web;

import java.util.ArrayList;

import de.hsadmin.model.TestDR;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class HSTab extends VerticalLayout{
	
	private static final long serialVersionUID = 8755003327696243905L;
	
	//Just for testing purposes
	private static final String USER_NAME = "hsh98";
	private static final String PASSWORD = "Diego.R!";
	
	private HorizontalLayout panelToolbar;
	private Table grid;
	
	public HSTab(String source){
		super();
		panelToolbar = new PanelToolbar(source);
		addComponent(panelToolbar);
		setComponentAlignment(panelToolbar, Alignment.MIDDLE_RIGHT);
		addComponent(getGrid());
	}

	private Table getGrid() {
		grid = new Table();

		grid.addContainerProperty("Id", String.class, null);
		grid.addContainerProperty("Description", String.class, null);

		grid.setPageLength(grid.size());
		//pkgList.addItemClickListener(this);
		grid.setSelectable(true);
		grid.setImmediate(true);
		grid.setSizeFull();

		return grid;
	}
	
	public void fillTable(){
		TestDR dataSource = new TestDR();
		
		dataSource.init(USER_NAME, PASSWORD);
		ArrayList<String> result = dataSource.getResult("emailaddress","search");
		
		int itemId = 1;
		for(String item : result){
			int token = item.indexOf(":");
			if(token != -1){
				String emailAddress = item.substring(0, token);
				item = item.substring(token +1, item.length());
				token = item.indexOf(",");
				while(token != -1){
					String target = item.substring(0, token);
					item = item.substring(token +1, item.length());
					grid.addItem(new Object[]{emailAddress, target}, itemId);
					
					token = item.indexOf(",");
					itemId++;
				}
			}
		}
	}

}
