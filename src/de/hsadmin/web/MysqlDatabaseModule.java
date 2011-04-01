package de.hsadmin.web;

public class MysqlDatabaseModule extends DatabaseModule {

	private static final long serialVersionUID = 1L;

	@Override
	public String[] getEncodings() {
		return new String[] { "utf8", "latin1" };
	}

	@Override
	public String getModuleIdent() {
		return "mysqldb";
	}

	@Override
	public String getUserModuleIdent() {
		return "mysqluser";
	}

}
