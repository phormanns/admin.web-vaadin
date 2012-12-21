package de.hsadmin.web.vaadin;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.DateField;
import com.vaadin.ui.PopupDateField;

import de.hsadmin.web.AbstractProperty;
import de.hsadmin.web.DateProperty;
import de.hsadmin.web.StringProperty;
import de.hsadmin.web.XmlrpcProperty;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

public class DatePropertyFieldFactory implements PropertyFieldFactory {

	public static final DateFormat serverDf = DateFormat.getDateInstance(DateFormat.SHORT);
	
	private boolean readOnly = false;
	private boolean writeOnce = false;
	
	@Override
	public Object createFieldComponent(PropertyConfig prop, XmlrpcProperty value) {
		DateField dateField = new PopupDateField(prop.getLabel());
		dateField.setDateFormat("dd.MM.yyyy");
		dateField.setData(prop.getId());
		dateField.setWidth(480.0f, Sizeable.UNITS_PIXELS);
		try {
			if (value != null) {
				if (value instanceof AbstractProperty) {
					if (value instanceof StringProperty) {
						dateField.setValue(DateProperty.serverDf.parse(((StringProperty) value).toStringValue()));
					}
					if (value instanceof DateProperty) {
						dateField.setValue(((DateProperty) value).getDateValue());
					}
				}
				dateField.setReadOnly(isReadOnly());
				return dateField;
			}
		} catch (ReadOnlyException e) {
		} catch (ConversionException e) {
		} catch (ParseException e) {
		}
		dateField.setReadOnly(isReadOnly());
		return dateField;
	}

	@Override
	public AbstractProperty getValue(PropertyConfig prop, Object component) {
		if (component instanceof DateField) {
			return (new StringProperty(serverDf.format((Date) ((DateField) component).getValue()))); // TODO: besser DateProperty ?
		}
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setWriteOnce(boolean writeOnce) {
		this.writeOnce = writeOnce;
	}

	@Override
	public boolean isWriteOnce() {
		return writeOnce;
	}

}
