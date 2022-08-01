package com.mocker.mockme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ResponseLoader
{
	@Autowired
	public ResponseLoader(ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;
	}

	ResourceLoader resourceLoader;
	public Resource loadResponse(String endpoint) {
		return resourceLoader.getResource(
						"classpath:responses" + endpoint + "/response.json");
	}
}
