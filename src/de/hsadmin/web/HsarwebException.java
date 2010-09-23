package de.hsadmin.web;


public class HsarwebException extends Exception {

	private static final long serialVersionUID = 1L;

	public HsarwebException(String msg, Exception e) {
		super(msg, e);
	}

	public HsarwebException(Exception e) {
		super(e);
	}

	public HsarwebException(String msg) {
		super(msg);
	}

}
