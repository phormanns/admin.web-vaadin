package de.hsadmin.web;

public class PgsqlUserModule extends DatabaseUserModule {

	private static final long serialVersionUID = 1L;

	@Override
	public String getModuleIdent() {
		return "postgresqluser";
	}

}