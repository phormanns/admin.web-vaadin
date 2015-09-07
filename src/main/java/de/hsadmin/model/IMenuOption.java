package de.hsadmin.model;

/**
 * This Interface has the names of the available options 
 * in hostsharing
 * @author druiz
 */
public interface IMenuOption {

	public static final String UNIX_USERS = "user";
	public static final String EMAIL_ADDRESSES = "emailaddress";
	public static final String EMAIL_ALIASES = "emailalias";
	public static final String MYSQL_DB = "mysqldb";
	public static final String MYSQL_USER = "mysqluser";
	public static final String POSTGRES_DB = "postgresqldb";
	public static final String POSTGRES_USER = "postgresqluser";
	public static final String QUEUED_TASKS = "q";
	public static final String DOMAINS = "domain";
	public static final String PACKAGE = "pac";
	public static final String MANAGED_SERVERS = "hive";
	public static final String CUSTOMER = "customer";
	
}
