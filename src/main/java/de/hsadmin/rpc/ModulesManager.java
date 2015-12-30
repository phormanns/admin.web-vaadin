package de.hsadmin.rpc;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import de.hsadmin.model.IRemote;


public class ModulesManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(ModulesManager.class);
	
	private final Map<String, ModuleInfo> modulesRepository; 
	private final Map<String, URL> serviceRepository; 
	
	public ModulesManager() {
		modulesRepository = new HashMap<String, ModuleInfo>();
		serviceRepository = new HashMap<String, URL>();
	}
	
	public boolean hasModule(final String name) {
		return modulesRepository.containsKey(name);
	}

	void add(final ModuleInfo module, final URL serverURL) {
		modulesRepository.put(module.getName(), module);
		serviceRepository.put(module.getName(), serverURL);
	}

	public ModuleInfo module(String moduleName) {
		return modulesRepository.get(moduleName);
	}
	
	public String[] entryPointColumns(String moduleName) {
		if (hasModule(moduleName) && "customer".equals(moduleName)) {
			return new String[] { "name", "memberNo" };
		}
		if (hasModule(moduleName) && "hive".equals(moduleName)) {
			return new String[] { "name", "description" };
		}
		if (hasModule(moduleName) && "domain".equals(moduleName)) {
			return new String[] { "name", "user" };
		}
		if (hasModule(moduleName) && "pac".equals(moduleName)) {
			return new String[] { "name", "customer" };
		}
		return new String[] { "name" };
	}

	public IRemote proxy(final String moduleName) {
		return new IRemote() {
			
			@SuppressWarnings("unchecked")
			@Override
			public List<Map<String, Object>> search(final String runAsUser, final String ticket, final Map<String, String> whereParams) throws XmlRpcException 
			{
				LOG.info("RPC Call: " + moduleName + ".search - user " + runAsUser);
				final XmlRpcClient rpcClient = rpcClient(moduleName);
				final List<Object> xmlRpcParamsList = new ArrayList<Object>();
				xmlRpcParamsList.add(runAsUser);
				xmlRpcParamsList.add(ticket);
				xmlRpcParamsList.add(whereParams);
				final Object[] rpcResult = (Object[]) rpcClient.execute(moduleName + ".search", xmlRpcParamsList);
				final List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
				for (final Object obj : rpcResult) {
					if (obj instanceof Map) {
						resultList.add((Map<String, Object>) obj);
					}
				}
				return resultList;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Map<String, Object> add(final String runAsUser, final String ticket, final Map<String, Object> setParams) throws XmlRpcException 
			{
				LOG.info("RPC Call: " + moduleName + ".add - user " + runAsUser);
				final XmlRpcClient rpcClient = rpcClient(moduleName);
				final List<Object> xmlRpcParamsList = new ArrayList<Object>();
				xmlRpcParamsList.add(runAsUser);
				xmlRpcParamsList.add(ticket);
				xmlRpcParamsList.add(setParams);
				return (Map<String, Object>) rpcClient.execute(moduleName + ".add", xmlRpcParamsList);
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<Map<String, Object>> update(final String runAsUser, final String ticket, final Map<String, Object> setParams, final Map<String, String> whereParams)
					throws XmlRpcException 
			{
				LOG.info("RPC Call: " + moduleName + ".update - user " + runAsUser);
				final XmlRpcClient rpcClient = rpcClient(moduleName);
				final List<Object> xmlRpcParamsList = new ArrayList<Object>();
				xmlRpcParamsList.add(runAsUser);
				xmlRpcParamsList.add(ticket);
				xmlRpcParamsList.add(setParams);
				xmlRpcParamsList.add(whereParams);
				final Object[] rpcResult = (Object[]) rpcClient.execute(moduleName + ".update", xmlRpcParamsList);
				final List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
				for (final Object obj : rpcResult) {
					if (obj instanceof Map) {
						resultList.add((Map<String, Object>) obj);
					}
				}
				return resultList;
			}

			@Override
			public void delete(final String runAsUser, final String ticket, final Map<String, String> whereParams) throws XmlRpcException 
			{
				LOG.info("RPC Call: " + moduleName + ".delete - user " + runAsUser);
				final XmlRpcClient rpcClient = rpcClient(moduleName);
				final List<Object> xmlRpcParamsList = new ArrayList<Object>();
				xmlRpcParamsList.add(runAsUser);
				xmlRpcParamsList.add(ticket);
				xmlRpcParamsList.add(whereParams);
				rpcClient.execute(moduleName + ".delete", xmlRpcParamsList);
			}

			private XmlRpcClient rpcClient(final String moduleName) 
			{
				final XmlRpcClient rpcClient = new XmlRpcClient();
				final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
				config.setServerURL(serviceRepository.get(moduleName));
				config.setEnabledForExtensions(true);
				rpcClient.setConfig(config);
				return rpcClient;
			}
		};
	}
	
}
