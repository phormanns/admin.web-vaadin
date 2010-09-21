package de.hsadmin.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

@ManagedBean
@SessionScoped
public class Context {

	private ExternalContext getContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public String getUser() {
		Assertion assertion = (Assertion) getContext().getSessionMap().get(AbstractCasFilter.CONST_CAS_ASSERTION);
		AttributePrincipal principal = assertion.getPrincipal();
		return principal.getName();
	}

}
