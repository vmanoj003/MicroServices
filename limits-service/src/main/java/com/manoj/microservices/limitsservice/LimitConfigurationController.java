package com.manoj.microservices.limitsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manoj.microservices.limitsservice.bean.Configuration;
import com.manoj.microservices.limitsservice.bean.LimitConfiguration;

@RestController
public class LimitConfigurationController {
	
	@Autowired
	private Configuration configuration;
	
	@GetMapping("/limits")
	public LimitConfiguration returnLimitsFromConfigurations() {
		
		return new LimitConfiguration(configuration.getMaximum(),configuration.getMinimum());
	}

}
