package de.hsadmin.web;

public class EntryPointsFactory extends AbstractEntryPointsFactory {

	@Override
	public String[] getEntryPointNames(final String role) {
		if ("HOSTMASTER".equals(role)) {
			return new String[] { "customer", "pac", "domain" };
		}
		if ("CUSTOMER".equals(role)) {
			return new String[] { "customer", "pac", "domain" };
		}
		if ("PAC_ADMIN_DW".equals(role)) {
			return new String[] { "pac", "domain" };
		}
		if ("DOM_ADMIN".equals(role)) {
			return new String[] { "domain" };
		}
		return new String[] { };
	}

}
