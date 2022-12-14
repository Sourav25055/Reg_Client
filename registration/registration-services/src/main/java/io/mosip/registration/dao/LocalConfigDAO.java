package io.mosip.registration.dao;

import java.util.List;
import java.util.Map;

import io.mosip.registration.entity.PermittedLocalConfig;

public interface LocalConfigDAO {

	public List<PermittedLocalConfig> getAllPermittedLocalConfigs();
	public List<String> getPermittedJobs(String configType);
	public List<String> getPermittedConfigurations(String configType);
	public void modifyJob(String name, String value);
	public void modifyConfigurations(Map<String, String> localPreferences);
	public Map<String, String> getLocalConfigurations();
	public String getValue(String name);
	public void updateShortcutPreference(String name, String value);
	
}
