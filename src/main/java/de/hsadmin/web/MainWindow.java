package de.hsadmin.web;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MainWindow extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private static AbstractSplitPanel content;

	public MainWindow() {

		setSizeFull();

		Panel mainPanel = new Panel();
		mainPanel.setSizeFull();
		VerticalLayout vl = new VerticalLayout();
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.addStyleName("panelstyle");
		hl.setSpacing(true);
		hl.addComponent(new Label("HS Admin Main Window"));

		//Customization Button to set up the left panel
		Button customButton = new Button();
		customButton.setStyleName("tiny");
		customButton.setIcon(new ThemeResource("../icons/settings-icon.png"));
		content = new HorizontalSplitPanel();

		customButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
            public void buttonClick(final ClickEvent event) {
				content.setSecondComponent(CustomizationPanel.getInstance());
            }
        });
		hl.addComponent(customButton);
		
		vl.addComponent(hl);
		
		//final AbstractSplitPanel content = new HorizontalSplitPanel();
		content.setSizeFull();
		vl.addComponent(content);

		final EntryPointsSelector entryPoints = new EntryPointsSelector();
		entryPoints.setSizeFull();
		entryPoints.setSizeFull();
		content.setFirstComponent(entryPoints);

		content.setSecondComponent(new MainPanel());

		content.setSplitPosition(30.0f);
		setCompositionRoot(mainPanel);
		mainPanel.setContent(vl);

	}

	public static void setCenterPanel(String source) {
		AbstractFactory panelFactory = FactoryProducer.getFactory("panel");
		content.setSecondComponent(panelFactory.getPanel(source));
	}
}
