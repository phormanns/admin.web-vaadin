package de.hsadmin.model;

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

import de.hsadmin.rpc.RpcException;

public class TestDR {

	final XmlRpcClient client = new XmlRpcClient();
	final List<Serializable> xmlRpcParamsList = new ArrayList<Serializable>();

	/** 
	 * Do a single call to the hsadmin api.
	 * @param args expects username and password
	 * @throws RpcException 
	 *  
	 */
	public void init(String user, String pwd) throws RpcException {
		try {
			final String username = user;
			final String password = pwd;
			final TicketService ticketService = new TicketService();
			final String grantingTicket = ticketService.getGrantingTicket(username, password);
			
			final String ticket = ticketService.getServiceTicket(grantingTicket);
			
			final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL("https://config.hostsharing.net:443/hsar/xmlrpc/hsadmin"));
			config.setEnabledForExtensions(true);

			client.setConfig(config);
			
//			Object rpcResult = client.execute("system.listMethods", new ArrayList());
//			Object[] resultArray = (Object[]) rpcResult;
//			for (Object obj : resultArray) {
//				System.out.println(obj);
//			}
			
			xmlRpcParamsList.add(username);
			xmlRpcParamsList.add(ticket);
			final HashMap<String, Serializable> whereParamsMap = new HashMap<String, Serializable>();
			xmlRpcParamsList.add(whereParamsMap);
			/*final Object[] rpcResult = (Object[]) client.execute("emailaddress.search", xmlRpcParamsList);
			for (final Object resObject : rpcResult) {
				@SuppressWarnings("unchecked")
				final Map<String, Serializable> emailaddressData = (Map<String, Serializable>) resObject;
				System.out.print(emailaddressData.get("emailaddress") + " : ");
				final Object[] targets = (Object[]) emailaddressData.get("target");
				for (Object target : targets) {
					System.out.print(target + ",");
				}
				System.out.println();
			}*/
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getResult(String query, String type){
		ArrayList<String> list = new ArrayList<String>();
		Object[] rpcResult;
		try {
			rpcResult = (Object[]) client.execute(query+"."+type, xmlRpcParamsList);
			StringBuilder item;
			for (final Object resObject : rpcResult) {
				item = new StringBuilder();
				@SuppressWarnings("unchecked")
				final Map<String, Serializable> emailaddressData = (Map<String, Serializable>) resObject;
				item.append(emailaddressData.get(query) + " : ");
				final Object[] targets = (Object[]) emailaddressData.get("target");
				for (Object target : targets) {
					item.append(target + ",");
				}
				list.add(item.toString());
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
		
	}
	
}
