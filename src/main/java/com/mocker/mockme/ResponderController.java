package com.mocker.mockme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@RestController(value = "*")
public class ResponderController
{
	public ResponderController(ResponseLoader responseLoader)
	{
		this.responseLoader = responseLoader;
	}

	ResponseLoader responseLoader;

	@GetMapping("/**")
	public String testResponse(HttpServletRequest request) throws IOException, ResponseStatusException
	{

		InputStream resource = responseLoader.getFile();

		ObjectMapper objectMapper = new ObjectMapper();
		List<Endpoint> list = Arrays.stream(objectMapper.readValue(resource, Endpoint[].class)).filter(
						endpoint -> endpoint.endpoint().equals(request.getServletPath())).toList();
		if (list.isEmpty())
		{
			throw new ResponseStatusException(
							HttpStatus.NOT_FOUND, "Endpoint Not Found");
		}
		return list.get(0).response();
	}

	@PostMapping(value = "/addfile")
	public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException
	{
		responseLoader.addFile(new ByteArrayResource(file.getBytes()));

		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
