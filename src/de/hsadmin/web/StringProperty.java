package de.hsadmin.web;

public class StringProperty extends AbstractProperty {
	
	public String property ;

	public StringProperty(String property) {
		this.property = property;
	}

	@Override
	public Object toXmlrpcParam() {
		return property;
	}

}
