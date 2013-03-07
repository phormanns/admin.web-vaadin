package de.hsadmin.web;

public class HsarwebUserException extends HsarwebException {
	
	private static final long serialVersionUID = 1L;

	public HsarwebUserException(String string, Throwable e) {
		super(string, e);
	}

	public HsarwebUserException(Throwable e) {
		super(e);
	}

	public HsarwebUserException(String string) {
		super(string);
	}

}
