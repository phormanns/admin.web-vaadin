package de.hsadmin.web;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class InfoWindow extends Window {

	private static final long serialVersionUID = 1L;

	public InfoWindow(final String info) 
	{
		super("Info");
		center();
		final VerticalLayout subContent = new VerticalLayout();
		subContent.setMargin(true);
		subContent.addComponent(new Label(info));
		final Button ok = new Button("OK");
		ok.setClickShortcut(KeyCode.ENTER);
		ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() 
		{
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		subContent.addComponent(ok);
		setContent(subContent);
	}
}