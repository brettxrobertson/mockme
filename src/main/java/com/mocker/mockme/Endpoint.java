package com.mocker.mockme;

import java.util.Map;

public record Endpoint(String endpoint, Map<String, String> inputs, Boolean validate, String response)
{
}


