package de.hsadmin.rpc;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import de.hsadmin.model.IRemote;


public class ModulesManager {

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

	public IRemote proxy(final String moduleName) {
		return new IRemote() {
			
			@SuppressWarnings("unchecked")
			@Override
			public List<Map<String, Object>> search(final String runAsUser, final String ticket,
					final Map<String, String> whereParams) throws XmlRpcException {
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
			public Map<String, Object> add(String runAsUser, String ticket,
					Map<String, Object> setParams) throws XmlRpcException {
				final XmlRpcClient rpcClient = rpcClient(moduleName);
				final List<Object> xmlRpcParamsList = new ArrayList<Object>();
				xmlRpcParamsList.add(runAsUser);
				xmlRpcParamsList.add(ticket);
				xmlRpcParamsList.add(setParams);
				return (Map<String, Object>) rpcClient.execute(moduleName + ".add", xmlRpcParamsList);
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<Map<String, Object>> update(String runAsUser, String ticket,
					Map<String, Object> setParams, Map<String, String> whereParams)
					throws XmlRpcException {
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
			public void delete(String runAsUser, String ticket,
					Map<String, String> whereParams) throws XmlRpcException {
				final XmlRpcClient rpcClient = rpcClient(moduleName);
				final List<Object> xmlRpcParamsList = new ArrayList<Object>();
				xmlRpcParamsList.add(runAsUser);
				xmlRpcParamsList.add(ticket);
				xmlRpcParamsList.add(whereParams);
				rpcClient.execute(moduleName + ".delete", xmlRpcParamsList);
			}

			private XmlRpcClient rpcClient(final String moduleName) {
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
