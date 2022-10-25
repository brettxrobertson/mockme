package com.mocker.mockme;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class ResponseLoader
{

	private ByteArrayResource file;

	public void addFile(ByteArrayResource endpointFile)
	{
		this.file = endpointFile;
	}

	public InputStream getFile() throws ResponseStatusException
	{
		if (file == null)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No endpoints added");
		}

		return new ByteArrayInputStream(file.getByteArray());

	}
}
