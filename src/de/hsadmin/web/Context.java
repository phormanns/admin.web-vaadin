package de.hsadmin.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

@ManagedBean(name="context")
@SessionScoped
public class Context {

	private ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	private AttributePrincipal getPrincipal() {
		ExternalContext context = getExternalContext();
		Assertion assertion = (Assertion) context.getSessionMap().get(AbstractCasFilter.CONST_CAS_ASSERTION);
		return assertion.getPrincipal();
	}

	public String getUser() {
		return getPrincipal().getName();
	}

	public String getProxyTicket() {
		return getPrincipal().getProxyTicketFor("https://agnes.ostwall195.de:9443/hsar/backend");
	}

	public String getContextPath() {
		return getExternalContext().getRequestContextPath();
	}
}
