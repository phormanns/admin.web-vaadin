package de.hsadmin.rpc;


public class RpcException extends Exception {

	private static final long serialVersionUID = 1L;

	public RpcException(final Exception e) {
		super(e);
	}

	public RpcException(final String message) {
		super(message);
	}
}
