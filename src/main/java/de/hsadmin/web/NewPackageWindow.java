package main.java.de.hsadmin.web;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class NewPackageWindow extends Window implements IHSWindow{

	private static final long serialVersionUID = 1L;
	public NewPackageWindow() {
	       super("New");
	       center();
	       setModal(true);
	       setWidth("350px");
	       
	       FormLayout subContent = new FormLayout();
	       subContent.setMargin(true);

	       subContent.addComponent(new Label("Server:"));
	       subContent.addComponent(new TextField("ID"));
	       subContent.addComponent(new TextArea("Description:"));
	       subContent.addComponent(new HSConfirmBox());

	       setContent(subContent);
	    }

}
