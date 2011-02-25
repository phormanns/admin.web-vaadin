package de.hsadmin.web;

import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;

public class UnixUserModule extends GenericModule {

	private static final long serialVersionUID = 1L;
	
	private ModuleConfig moduleConfig;

	public UnixUserModule() {
		moduleConfig = new ModuleConfig("user");
		PropertyConfig propId = new PropertyConfig(moduleConfig, "id", Long.class, true, true);
		moduleConfig.addProperty(propId);
		PropertyConfig propUserId = new PropertyConfig(moduleConfig, "userid", Long.class, true);
		moduleConfig.addProperty(propUserId);
		PropertyConfig propUserName = new PropertyConfig(moduleConfig, "name", String.class);
		moduleConfig.addProperty(propUserName);
		PropertyConfig propUserComment = new PropertyConfig(moduleConfig, "comment", String.class);
		moduleConfig.addProperty(propUserComment);
		PropertyConfig propShell = new PropertyConfig(moduleConfig, "shell", String.class);
		moduleConfig.addProperty(propShell);
		PropertyConfig propHomeDir = new PropertyConfig(moduleConfig, "homedir", String.class, true);
		moduleConfig.addProperty(propHomeDir);
		PropertyConfig propPacket = new PropertyConfig(moduleConfig, "pac", String.class, true);
		moduleConfig.addProperty(propPacket);
		PropertyConfig propSoftQuota = new PropertyConfig(moduleConfig, "quota_softlimit", Long.class, true);
		moduleConfig.addProperty(propSoftQuota);
		PropertyConfig propHardQuota = new PropertyConfig(moduleConfig, "quota_hardlimit", Long.class, true);
		moduleConfig.addProperty(propHardQuota);
	}
	
	@Override
	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}

}
