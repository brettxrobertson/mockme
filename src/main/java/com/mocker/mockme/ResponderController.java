package com.mocker.mockme;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RestController(value="*")
public class ResponderController
{
	public ResponderController(ResponseLoader responseLoader)
	{
		this.responseLoader = responseLoader;
	}
	ResponseLoader responseLoader;

	@GetMapping("/*")
	public String testResponse(HttpServletRequest request) throws IOException
	{
		InputStream resource = responseLoader.loadResponse(request.getServletPath()).getInputStream();

		return IOUtils.toString(resource, Charset.defaultCharset());

	}


}
