package com.mocker.mockme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@RestController(value = "*")
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
		InputStream resource = responseLoader.loadResponse().getInputStream();

		ObjectMapper objectMapper = new ObjectMapper();
		return Arrays.stream(objectMapper.readValue(resource, Endpoint[].class)).filter(
										endpoint -> endpoint.endpoint().equals(request.getServletPath())).toList()
						.get(0).response();
	}

	@GetMapping(value = "/get-file",
					produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void getFile(HttpServletResponse response) throws IOException
	{
		InputStream resource = responseLoader.loadResponse().getInputStream();
		resource.transferTo(response.getOutputStream());

		response.setContentType("application/json");
		response.addHeader("Content-Disposition", "attachment; filename=endpoints.json");

		response.getOutputStream().flush();
	}
}
