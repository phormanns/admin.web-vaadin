package de.hsadmin.rpc;

import de.hsadmin.model.TicketService;

public interface HSAdminSession {

	public String getGrantingTicket();
	
	public TicketService getTicketService();
	
	public ModulesManager getModulesManager();

	public String getUser();
	
}
