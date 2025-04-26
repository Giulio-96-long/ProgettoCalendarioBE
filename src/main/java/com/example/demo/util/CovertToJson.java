package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CovertToJson {
	
	 private static final ObjectMapper objectMapper = new ObjectMapper();

	    public static String toJson(Object obj) {
	        if (obj == null) {
	            return null;
	        }
	        try {
	            return objectMapper.writeValueAsString(obj);
	        } catch (JsonProcessingException e) {
	            // Prova a convertirlo in un toString() se fallisce
	            return "\"ConversionError: " + obj.toString() + "\"";
	        }
	    }
}
