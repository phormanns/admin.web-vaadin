package de.hsadmin.web;

public class StringProperty extends AbstractProperty {
	
	final public String property ;

	public StringProperty(String property) {
		this.property = property;
	}

	public StringProperty(Object value) {
		String temp = "undefined";
		if (value instanceof String) {
			temp = (String) value;
		}
		this.property = temp;
	}

	@Override
	public Object toXmlrpcParam() {
		return property;
	}

	@Override
	public String toStringValue() {
		return property;
	}

}
