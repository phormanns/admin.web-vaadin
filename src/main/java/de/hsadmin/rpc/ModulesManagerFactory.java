package de.hsadmin.rpc;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import de.hsadmin.model.TicketService;

public class ModulesManagerFactory {
	
	private final String ticketGrantingTicket;
	private final String runAsUser;
	
	public ModulesManagerFactory(final String ticketGrantingTicket, final String runAsUser) {
		this.ticketGrantingTicket = ticketGrantingTicket;
		this.runAsUser = runAsUser;
	}
	
	public ModulesManager newModulesManager(final String... serverURLs) throws RpcException {
		final ModulesManager moduleManager = new ModulesManager();
		final TicketService ticketService = new TicketService();
		try {
			for (final String servername : serverURLs) 
			{
				final XmlRpcClient rpcClient = new XmlRpcClient();
				final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
				final URL serverURL = new URL(servername);
				config.setServerURL(serverURL);
				config.setEnabledForExtensions(true);
				rpcClient.setConfig(config);
				final List<Serializable> xmlRpcParamsList = new ArrayList<Serializable>();
				xmlRpcParamsList.add(runAsUser);
				xmlRpcParamsList.add(ticketService.getServiceTicket(ticketGrantingTicket));
				final HashMap<String, Serializable> whereParamsMap = new HashMap<String, Serializable>();
				xmlRpcParamsList.add(whereParamsMap);
				final Object[] rpcResult = (Object[]) rpcClient.execute("property.search", xmlRpcParamsList);
				for (final Object resObject : rpcResult) {
					@SuppressWarnings("unchecked")
					final Map<String, Serializable> propertyData = (Map<String, Serializable>) resObject;
					final String moduleName = (String) propertyData.get("module");
					if (!moduleManager.hasModule(moduleName)) {
						moduleManager.add(new ModuleInfo(moduleName), serverURL);
					}
					final PropertyInfo propInfo = new PropertyInfo();
					propInfo.setDisplaySequence(Integer.parseInt((String) propertyData.get("displaySequence")));
					propInfo.setDisplayVisible((String) propertyData.get("displayVisible"));
					propInfo.setMaxLength(Integer.parseInt((String) propertyData.get("maxLength")));
					propInfo.setMinLength(Integer.parseInt((String) propertyData.get("minLength")));
					propInfo.setModule(moduleName);
					propInfo.setName((String) propertyData.get("name"));
					propInfo.setReadwriteable((String) propertyData.get("readwriteable"));
					propInfo.setSearchable((String) propertyData.get("searchable"));
					propInfo.setType((String) propertyData.get("type"));
					propInfo.setValidationRegexp((String) propertyData.get("validationRegexp"));
					moduleManager.module(moduleName).add(propInfo);
				}
			}
		} catch (MalformedURLException | XmlRpcException e) {
			throw new RpcException(e);
		}
		return moduleManager;
	}

}
