package de.hsadmin.web;

import java.util.Map;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;


public interface IEditorFactory {

	public abstract IHSEditor getEditor(String action, PropertyInfo propertyInfo);

	public abstract IHSEditor getEditor(String action, PropertyInfo propertyInfo, HSAdminSession session, Map<String, String> whereContext);
	
}
