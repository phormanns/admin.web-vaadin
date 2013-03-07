package de.hsadmin.web;

public class HsarwebInternalException extends HsarwebException {
	
	private static final long serialVersionUID = 1L;

	public HsarwebInternalException(String string, Throwable e) {
		super(string, e);
	}

	public HsarwebInternalException(Throwable e) {
		super(e);
	}

	public HsarwebInternalException(String string) {
		super(string);
	}

}
