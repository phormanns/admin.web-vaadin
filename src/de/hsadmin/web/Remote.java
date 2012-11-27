package de.hsadmin.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Remote {

	private XmlRpcClient client;
	private MainApplication app;
	
	public Remote(MainApplication application) {
		this.app = application;
	}

	public Object callSearch(String module, Map<String, AbstractProperty> where) throws HsarwebException {
		return xmlrpcCall(module, "search", where); 
	}

	public void callAdd(String module, Map<String, AbstractProperty> set) throws HsarwebException {
		xmlrpcCall(module, "add", set); 
	}

	public void callUpdate(String module, Map<String, AbstractProperty> set, Map<String, AbstractProperty> where) throws HsarwebException {
		xmlrpcCall(module, "update", set, where); 
	}

	public void callDelete(String module, Map<String, AbstractProperty> where) throws HsarwebException {
		xmlrpcCall(module, "delete", where); 
	}

	private Object xmlrpcCall(String module, String operation, Map<String, AbstractProperty> param1) throws HsarwebException {
		Object[] params = new Object[3];
		params[0] = app.getLogin();
		params[1] = app.getProxyTicket();
		params[2] = param1 != null ? param1 : new HashMap<String, String>();
		return xmlrpcCall(module + "." + operation, params);
	}

	private Object xmlrpcCall(String module, String operation, Map<String, AbstractProperty> param1, Map<String, AbstractProperty> param2) throws HsarwebException {
		Object[] params = new Object[4];
		params[0] = app.getLogin();
		params[1] = app.getProxyTicket();
		params[2] = param1 != null ? param1 : new HashMap<String, String>();
		params[3] = param2 != null ? param2 : new HashMap<String, String>();
		return xmlrpcCall(module + "." + operation, params);
	}

	private Object xmlrpcCall(String operation, Object[] params) throws HsarwebException {
		Object res = null;
		try {
			res = getClient().execute(operation, params);
		} catch (XmlRpcException e) {
			throw new HsarwebException(e.getMessage(), e);
		}
		return res;
	}

	private XmlRpcClient getClient() throws HsarwebException {
		if (client == null) {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			try {
				String xmlrpcURL = app.getContextParam("xmlrpcURL");
				config.setServerURL(new URL(xmlrpcURL));
				config.setEnabledForExtensions(true);
				client = new XmlRpcClient();
				client.setConfig(config);
			} catch (MalformedURLException e) {
				throw new HsarwebException("error in remote server url", e);
			}
		}
		return client;
	}

}
