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
	private ByteArrayResource endpoints;
	private ByteArrayResource validator;

	public void addFile(FileType fileType,ByteArrayResource file) throws ResponseStatusException
	{
		switch(fileType)
		{
			case ENDPOINTS:
				setEndpoints(file);
				break;
			case VALIDATOR:
				setValidator(file);
				break;
			default:
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Unknown file type");
		}
	}

	public InputStream getEndpoints() throws ResponseStatusException
	{
		if (endpoints == null)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No endpoints added");
		}

		return new ByteArrayInputStream(endpoints.getByteArray());

	}

	public InputStream getValidator() throws ResponseStatusException {
		if (validator == null) {
			return null;
		}

		return (new ByteArrayInputStream(validator.getByteArray()));
	}

	private void setEndpoints(ByteArrayResource file) {
		endpoints = file;
		//TODO: validate the file
	}

	private void setValidator(ByteArrayResource file){
		validator = file;
	}
}
