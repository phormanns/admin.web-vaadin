package de.hsadmin.web;


public class HsarwebException extends Exception {

	private static final long serialVersionUID = 1L;

	public HsarwebException(String string, Throwable e) {
		super(string, e);
	}

	public HsarwebException(Throwable e) {
		super(e);
	}

	public HsarwebException(String string) {
		super(string);
	}

}
