package de.hsadmin.web;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class Config {

	private static Config instance;

	private Properties props;
	
	private Config() {
		props = new Properties();
		File file = new File(System.getProperty("user.dir") + "/hsadmin.properties");
		if (!file.canRead()) {
			file = new File(System.getProperty("user.dir") + "/conf/hsadmin.properties");
		}
		if (!file.canRead()) {
			file = new File(System.getProperty("user.home") + "/.hsadmin.properties");
		}
		if (!file.canRead()) {
			file = new File("/etc/hsadmin.properties");
		}
		if (!file.canRead()) {
			file = new File("/etc/hsadmin/hsadmin.properties");
		}
		if (file.canRead()) {
			try {
				props.load(new FileReader(file));
			} catch (Exception e) {
				// should not happen
				e.printStackTrace();
			}
		}
	}
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	public String getProperty(String propertyName) {
		String property = props.getProperty(propertyName);
		if (property == null) {
			return null;
		}
		return property.trim();
	}
	
	public String getProperty(String propertyName, String defaultValue) {
		return props.getProperty(propertyName, defaultValue).trim();
	}
	
}
