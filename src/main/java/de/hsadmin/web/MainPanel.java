package de.hsadmin.web;

import java.util.ResourceBundle;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class MainPanel extends CustomComponent{

	private static final long serialVersionUID = -1085100738394404620L;

	public MainPanel() {
		final ResourceBundle bundle = ResourceBundle.getBundle("de.hsadmin.web.main");
		final Label dialog = new Label();
		final String title = bundle.getString("main.panel.title");
		final Panel panel  = new Panel(title);
		final String text = bundle.getString("main.panel.text");
		dialog.setValue(text);
		panel.setContent(dialog);
		setCompositionRoot(panel);
	}
}
