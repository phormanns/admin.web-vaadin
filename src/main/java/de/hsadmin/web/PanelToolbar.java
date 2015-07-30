package main.java.de.hsadmin.web;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

public class PanelToolbar extends HorizontalLayout{
	
	private static final long serialVersionUID = 7361166876939709915L;
	private Button newBtn, editBtn, deleteBtn, refreshBtn, helpBtn;
	
	public PanelToolbar(){
		super();
		newBtn = createButton("New", "new", "New");
		addComponent(newBtn);
		
		editBtn = createButton("Edit", "edit", "Edit");
		addComponent(editBtn);
		
		deleteBtn = createButton("Delete", "trash", "Delete");
		addComponent(deleteBtn);
		
		refreshBtn = createButton("Refresh", "reload", "Refresh");
		addComponent(refreshBtn);
		
		helpBtn = createButton("Help", "question", "Help");
		addComponent(helpBtn);
	}
	
	private Button createButton(String name, String image, String tooltip){
		Button btn = new Button();
		//btn.setId(name+"-btn");
		if (image != null) 
		{
			btn.setIcon(new ThemeResource("../icons/"+image+"-icon.png"));
		}
		btn.setDescription(tooltip);
		btn.setStyleName("borderless");
		
		/*EventListener<Event> listener = createListener();
		
		btn.addEventListener(Events.ON_CLICK, listener);*/

		return btn;
	}

}
