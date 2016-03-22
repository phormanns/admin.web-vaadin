package de.hsadmin.web;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class HelpWindow extends Window {

	private static final long serialVersionUID = 1L;
	
	private static final String HELP_HTML_SNIPPET = "Contact us in case of further questions";

	public HelpWindow() {
		// Create a sub-window and set the content
		super("Help Window");
		// Center it in the browser window
		center();

		VerticalLayout subContent = new VerticalLayout();
		subContent.setMargin(true);

		subContent.addComponent(new Label(HELP_HTML_SNIPPET));
		Button ok = new Button("OK");
		ok.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6121701552072481416L;

			public void buttonClick(ClickEvent event) {
				close(); // Close the sub-window
			}
		});
		subContent.addComponent(ok);

		setContent(subContent);
	}
}