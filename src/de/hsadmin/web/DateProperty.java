package de.hsadmin.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateProperty extends AbstractProperty {

	public static final DateFormat serverDf = DateFormat.getDateInstance(DateFormat.SHORT);

	final private Date property;

	public DateProperty(Date propertyValue) {
		this.property = propertyValue;
	}
	
	public DateProperty(String propertyValue) {
		Date temp = null;
		try {
			temp = serverDf.parse(propertyValue);
		} catch (ParseException e) {
		}
		property = temp;
	}
	
	@Override
	public Object toXmlrpcParam() {
		return serverDf.format(property);
	}

	@Override
	public String toStringValue() {
		return serverDf.format(property);
	}

	public Date getDateValue() {
		return property;
	}

}
