package com.mc.Mozo.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.mc.Mozo.services.Endpoint;

@Configuration
public class Config extends ResourceConfig {
	
	public Config() {
		register(Endpoint.class);
	}

}
