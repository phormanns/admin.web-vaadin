package de.hsadmin.web;

import com.vaadin.ui.Window;

import de.hsadmin.rpc.HSAdminSession;

public abstract class AbstractWindowFactory {

	public abstract Window getSubWindow(HSTab parent, String type, String action, HSAdminSession session);

}
