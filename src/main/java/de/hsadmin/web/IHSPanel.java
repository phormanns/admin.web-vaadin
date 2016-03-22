package de.hsadmin.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import de.hsadmin.rpc.RpcException;

public interface IHSPanel extends Component{

	public TabSheet createTabs(Object itemId) throws RpcException;
	
}