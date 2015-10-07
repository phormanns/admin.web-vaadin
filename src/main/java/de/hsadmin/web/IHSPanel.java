package de.hsadmin.web;

import java.util.ResourceBundle;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.RpcException;

public interface IHSPanel extends Component{

	ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages");

	public TabSheet createTabs(Object itemId) throws RpcException;
	
}