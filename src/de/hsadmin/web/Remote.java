package de.hsadmin.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

@ManagedBean(name="remote")
@SessionScoped
public class Remote {

	private XmlRpcClient client;
	
	@ManagedProperty(value="#{context}")
	private Context context;

	public Remote() throws HsarwebException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL("https://agnes.ostwall195.de:9443/hsar/xmlrpc/hsadmin"));
		} catch (MalformedURLException e) {
			throw new HsarwebException("error in remote server url", e);
		}
		client = new XmlRpcClient();
		client.setConfig(config);
	}
	
	public Object callSearch(String module, String user, Map<String, String> where) throws HsarwebException {
		Object[] params = new Object[3];
		params[0] = user;
		params[1] = context.getProxyTicket();
		params[2] = where;
		Object res;
		try {
			res = client.execute(module + ".search", params);
		} catch (XmlRpcException e) {
			throw new HsarwebException("error in remote server call", e);
		}
		return res; 
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
}
