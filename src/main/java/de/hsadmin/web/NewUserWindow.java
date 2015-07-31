package main.java.de.hsadmin.web;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class NewUserWindow extends Window implements IHSWindow{

	private static final long serialVersionUID = -6331851334050080215L;
	
	public NewUserWindow() {
       super("New");
       center();
       setModal(true);
       //setAction("show: slideDown;hide: slideUp");
       setWidth("350px");
       //setStyle("margin:5 5 5 5px");
       
       FormLayout subContent = new FormLayout();
       subContent.setMargin(true);

       subContent.addComponent(new Label("Packet:"));
       subContent.addComponent(new TextField("userpart"));
       subContent.addComponent(new PasswordField("Password"));
       subContent.addComponent(new PasswordField("Repeat Password"));
       subContent.addComponent(new TextArea("Comment:"));
       
       NativeSelect list = new NativeSelect("Shell");
       list.addItems("/usr/bin/passwd","/bin/csh","/bin/bash","/bin/zsh");
       list.setNullSelectionAllowed(false);
       list.setImmediate(true);
       
       subContent.addComponent(list);
       
       subContent.addComponent(new TextField("quota soft limit"));
       subContent.addComponent(new TextField("quota hard limit"));
       subContent.addComponent(new HSConfirmBox());

       setContent(subContent);
    }

}
