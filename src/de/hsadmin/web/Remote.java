package de.hsadmin.web;

import java.net.MalformedURLException;
import java.net.URL;
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

	public Object callSearch(String module, Map<String, String> where) throws HsarwebException {
		return xmlrpcCall(module, "search", where); 
	}

	public void callDelete(String module, Map<String, String> where) throws HsarwebException {
		xmlrpcCall(module, "delete", where); 
	}

	private Object xmlrpcCall(String module, String operation, Map<String, String> where) throws HsarwebException {
		Object[] params = new Object[3];
		params[0] = app.getLogin();
		params[1] = app.getProxyTicket();
		params[2] = where;
		Object res;
		try {
			res = getClient().execute(module + "." + operation, params);
		} catch (XmlRpcException e) {
			throw new HsarwebException("error in remote server call", e);
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
