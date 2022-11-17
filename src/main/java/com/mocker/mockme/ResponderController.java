package com.mocker.mockme;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController(value = "*") public class ResponderController
{
	ResponseLoader responseLoader;

	public ResponderController(ResponseLoader responseLoader)
	{
		this.responseLoader = responseLoader;
	}

	@GetMapping("/**") public String getResponder(HttpServletRequest request) throws IOException, ResponseStatusException
	{
		return genericResponder(request);
	}

	@PostMapping(path = "/**", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String postResponder(HttpServletRequest request, @RequestBody(required = false) String body) throws IOException, ResponseStatusException
	{
		return genericResponder(request, body);
	}

	public String genericResponder(HttpServletRequest request) throws IOException, ResponseStatusException
	{
		return genericResponder(request, null);
	}

	public String genericResponder(HttpServletRequest request, String body) throws IOException, ResponseStatusException
	{
		InputStream resource = responseLoader.getEndpoints();
		InputStream validator = responseLoader.getValidator();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

		List<Endpoint> list = new ArrayList<>();
		//TODO: add body validation and refactoring this
		if (StringUtils.isNotBlank(body) && validator != null)
		{
			// do some validator logic here
		}
		else
		{
			list = Arrays.stream(objectMapper.readValue(resource, Endpoint[].class)).filter(endpoint -> endpoint.endpoint().equals(request.getServletPath())).toList();

		}

		if (list.isEmpty())
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endpoint Not Found");
		}
		return list.get(0).response();
	}

	@PostMapping(value = "/upload/endpoints") public ResponseEntity<Object> handleEndpointsFileUpload(@RequestParam("file") MultipartFile file) throws IOException
	{
		responseLoader.addFile(FileType.ENDPOINTS, new ByteArrayResource(file.getBytes()));

		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/upload/validator") public ResponseEntity<Object> handleValidatorFileUpload(@RequestParam("file") MultipartFile file) throws IOException
	{
		responseLoader.addFile(FileType.VALIDATOR, new ByteArrayResource(file.getBytes()));
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
