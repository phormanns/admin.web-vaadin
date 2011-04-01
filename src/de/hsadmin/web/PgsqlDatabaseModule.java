package de.hsadmin.web;

public class PgsqlDatabaseModule extends DatabaseModule {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String[] getEncodings() {
		return new String[] { "UTF-8", "LATIN1" };
	}

	@Override
	public String getModuleIdent() {
		return "postgresqldb";
	}

	@Override
	public String getUserModuleIdent() {
		return "postgresqluser";
	}

}
