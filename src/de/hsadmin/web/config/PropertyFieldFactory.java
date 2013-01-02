package de.hsadmin.web.config;

import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.XmlrpcProperty;

public interface PropertyFieldFactory {

	public Object createFieldComponent(PropertyConfig prop, XmlrpcProperty value);
	
	public XmlrpcProperty getValue(PropertyConfig prop, Object component) throws HsarwebException;

	public void setReadOnly(boolean readOnly);

	public boolean isReadOnly();
	
	public void setWriteOnce(boolean writeOnce);
	
	public boolean isWriteOnce();

}
