package de.hsadmin.web;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;


public abstract class AbstractEditorFactory {

	public abstract IHSEditor getEditor(String action, PropertyInfo propertyInfo, String inputName, HSAdminSession session);
	
}
