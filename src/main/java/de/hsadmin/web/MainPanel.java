package de.hsadmin.web;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class MainPanel extends CustomComponent{

	private static final long serialVersionUID = -1085100738394404620L;

	public MainPanel() {
		final Label dialog = new Label();
		final Panel panel  = new Panel(I18N.getText("main.panel.title"));
		dialog.setValue(I18N.getText("main.panel.text"));
		panel.setContent(dialog);
		setCompositionRoot(panel);
	}
}
