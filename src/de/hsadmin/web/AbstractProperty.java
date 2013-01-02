package de.hsadmin.web;

public abstract class AbstractProperty implements XmlrpcProperty {

	@Override
	public abstract Object toXmlrpcParam();

	public abstract String toStringValue();

}
