package de.hsadmin.web.config;

import de.hsadmin.web.AbstractProperty;
import de.hsadmin.web.HsarwebException;

public interface PropertyFieldFactory {

	public Object createFieldComponent(PropertyConfig prop, Object value);
	
	public AbstractProperty getValue(PropertyConfig prop, Object component) throws HsarwebException;

	public void setReadOnly(boolean readOnly);

	public boolean isReadOnly();
	
	public void setWriteOnce(boolean writeOnce);
	
	public boolean isWriteOnce();

}
