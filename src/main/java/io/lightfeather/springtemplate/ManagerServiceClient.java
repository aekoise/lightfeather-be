package io.lightfeather.springtemplate;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import io.lightfeather.springtemplate.ApplicationConltroller.Manager;

public class ManagerServiceClient {

	private final RestTemplate restTemplate;
	private final Environment environment;

	public ManagerServiceClient(RestTemplate restTemplate, Environment environment) {
		this.restTemplate = restTemplate;
		this.environment = environment;
	}
	
	public Collection<Manager> getManagers() {
		String managerApiUrl= environment.getProperty("manager_service_url");
		Manager[] managers = restTemplate.getForObject(managerApiUrl, Manager[].class);
		return Arrays.asList(managers);
	}

}
