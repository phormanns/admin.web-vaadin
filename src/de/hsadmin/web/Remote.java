package de.hsadmin.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Remote {

	private XmlRpcClient client;
	private GenericModule module;
	
	public Remote(GenericModule module) {
		this.module = module;
	}

	public Object callSearch(String user, Map<String, String> where) throws HsarwebException {
		Object[] params = new Object[3];
		params[0] = user;
		params[1] = module.getProxyTicket();
		params[2] = where;
		Object res;
		try {
			res = getClient().execute(module.getModuleConfig().getName() + ".search", params);
		} catch (XmlRpcException e) {
			throw new HsarwebException("error in remote server call", e);
		}
		return res; 
	}
	
	private XmlRpcClient getClient() throws HsarwebException {
		if (client == null) {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			try {
				String xmlrpcURL = module.getContextParam("xmlrpcURL");
				config.setServerURL(new URL(xmlrpcURL));
			} catch (MalformedURLException e) {
				throw new HsarwebException("error in remote server url", e);
			}
			client = new XmlRpcClient();
			client.setConfig(config);
		}
		return client;
	}


}
