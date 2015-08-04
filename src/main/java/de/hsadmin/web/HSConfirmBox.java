package de.hsadmin.web;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

public class HSConfirmBox extends HorizontalLayout{

	private static final long serialVersionUID = 6110852472769497400L;
	Button okButton, cancelButton;
	
	public HSConfirmBox(){
		okButton = new Button("OK");
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6121701552072481416L;
			public void buttonClick(ClickEvent event) {
				//okButton Parent -> HSConfirmBox
				if(okButton.getParent() != null){
					//okButton.getParent() Parent -> FormLayout
					if(okButton.getParent().getParent() != null){
						//okButton.getParent().getParent() Parent -> Window
						if(okButton.getParent().getParent().getParent() != null){
							Window w = (Window) okButton.getParent().getParent().getParent();
							w.close();
						}
					}
				}
				//close(); // Close the sub-window
			}
		});
		cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6121701552072481416L;
			public void buttonClick(ClickEvent event) {
				if(cancelButton.getParent() != null){
					//cancelButton.getParent() Parent -> FormLayout
					if(cancelButton.getParent().getParent() != null){
						//cancelButton.getParent().getParent() Parent -> Window
						if(cancelButton.getParent().getParent().getParent() != null){
							Window w = (Window) cancelButton.getParent().getParent().getParent();
							w.close();
						}
					}
				}
			}
		});

		setSpacing(true);
		addComponent(okButton);
		addComponent(cancelButton);
	}
}
