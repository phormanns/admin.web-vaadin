package de.hsadmin.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hsadmin.model.TicketService;

public class ModulesManagerTest {

	private static final String USERNAME = "hsh98";
	private static final String PASSWORD = "Diego.R!";

	private ModulesManager modulesManager;
	private TicketService service;
	private String grantingTicket;

	@Before
	public void setUp() throws Exception {
		service = new TicketService();
		final String username = USERNAME;
		final String password = PASSWORD;
		grantingTicket = service.getGrantingTicket(username, password);
		final ModulesManagerFactory managerFactory = new ModulesManagerFactory(grantingTicket, username);
		modulesManager = managerFactory.newModulesManager("https://config1-test.hostsharing.net:443/hsar/xmlrpc/hsadmin");
	}

	@After
	public void tearDown() throws Exception {
		modulesManager = null;
	}

	@Test
	public void testCustomer() {
		try {
			testSearchCustomer();
		} catch (RpcException | XmlRpcException e) {
			System.err.println(e.getMessage());
		}
		try {
			testAddCustomer();
		} catch (XmlRpcException | RpcException e) {
			System.err.println(e.getMessage());
		}
		try {
			testUpdateCustomer();
		} catch (XmlRpcException | RpcException e) {
			System.err.println(e.getMessage());
		}
		try {
			testDeleteCustomer();
		} catch (XmlRpcException | RpcException e) {
			System.err.println(e.getMessage());
		}
	}

	public void testSearchCustomer() throws XmlRpcException, RpcException {
		final List<Map<String,Object>> list = modulesManager.proxy("customer").search(USERNAME, service.getServiceTicket(grantingTicket), new HashMap<String, String>());
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("hsh00-hsh", list.get(0).get("name"));
	}

	public void testAddCustomer() throws XmlRpcException, RpcException {
		final Map<String, Object> custData = new HashMap<String, Object>();
		custData.put("name", "hsh00-drz");
		custData.put("memberNo", "19019");
		final Map<String,Object> cust = modulesManager.proxy("customer").add(USERNAME, service.getServiceTicket(grantingTicket), custData);
		assertNotNull(cust);
		assertEquals("hsh00-drz", cust.get("name"));
		assertEquals("false", cust.get("free"));
	}

	public void testUpdateCustomer() throws XmlRpcException, RpcException {
		final Map<String, String> whereData = new HashMap<String, String>();
		final Map<String, Object> setData = new HashMap<String, Object>();
		whereData.put("name", "hsh00-drz");
		setData.put("free", "true");
		final List<Map<String,Object>> list = modulesManager.proxy("customer").update(USERNAME, service.getServiceTicket(grantingTicket), setData, whereData);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("true", list.get(0).get("free"));
	}

	public void testDeleteCustomer() throws XmlRpcException, RpcException {
		final Map<String, String> custData = new HashMap<String, String>();
		custData.put("name", "hsh00-drz");
		modulesManager.proxy("customer").delete(USERNAME, service.getServiceTicket(grantingTicket), custData);
	}

}
