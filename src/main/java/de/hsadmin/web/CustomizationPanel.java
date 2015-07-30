package main.java.de.hsadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import main.java.de.hsadmin.model.IMenuOption;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

/**
 * Panel to allow accordion customization.
 * Shows the available options and lets the user choose
 * Its defined as a singleton to have only one set of customizations
 * @author druiz
 */
public class CustomizationPanel extends CustomComponent{

	private static final long serialVersionUID = -416356626041492748L;
	private static CustomizationPanel custom = null;
	private ArrayList<String> availableOptions= new ArrayList<String>();
	private ArrayList<String> selectedOptions= new ArrayList<String>();
	
	private void initOptions() {
		availableOptions.add(IMenuOption.UNIX_USERS);
		availableOptions.add(IMenuOption.EMAIL_ADDRESSES);
		availableOptions.add(IMenuOption.EMAIL_ALIASES);
		availableOptions.add(IMenuOption.MYSQL_DB);
		availableOptions.add(IMenuOption.MYSQL_USER);
		availableOptions.add(IMenuOption.POSTGRES_DB);
		availableOptions.add(IMenuOption.POSTGRES_USER);
		availableOptions.add(IMenuOption.QUEUED_TASKS);
		availableOptions.add(IMenuOption.DOMAINS);
		availableOptions.add(IMenuOption.PACKAGE);
		availableOptions.add(IMenuOption.MANAGED_SERVERS);
		
		selectedOptions.add(IMenuOption.DOMAINS);
		selectedOptions.add(IMenuOption.PACKAGE);
		selectedOptions.add(IMenuOption.MANAGED_SERVERS);
	}
	
	private CustomizationPanel() {
		initOptions();
		final Panel panel  = new Panel("Main Panel");
		
		VerticalLayout vbox= new VerticalLayout();
		HorizontalLayout hbox;
        hbox = new HorizontalLayout();
        
        TwinColSelect twoColumns = new TwinColSelect();
        for(String optionName : availableOptions)
        	twoColumns.addItem(optionName);
        
        twoColumns.setValue(new HashSet<String>(selectedOptions));
        twoColumns.setNullSelectionAllowed(true);
        twoColumns.setMultiSelect(true);
        twoColumns.setImmediate(true);
        twoColumns.setLeftColumnCaption("Available options");
        twoColumns.setRightColumnCaption("Selected options");
        twoColumns.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4622912769038935768L;
			public void valueChange(ValueChangeEvent event) {
				@SuppressWarnings("unchecked")
				Collection<String> value = (Collection<String>) event.getProperty().getValue();
				selectedOptions = new ArrayList<String>(value);
				Notification.show("Value changed:",
		                "The new item will be added to the main panel",
		                Type.TRAY_NOTIFICATION);				
			}
		});
        hbox.addComponent(twoColumns);
        vbox.addComponent(hbox);
        
        panel.setContent(vbox);

		setCompositionRoot(panel);
	}
	
	public static CustomizationPanel getInstance(){
		if(custom==null)
			custom = new CustomizationPanel();
		return custom;
	}
	
	public ArrayList<String> getSelectedOptions(){
		return selectedOptions;
	}
}