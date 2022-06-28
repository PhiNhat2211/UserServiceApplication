package com.example.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseUtil {

    @Autowired
	private ObjectMapper mapper;

    // RESPONSE STATUS AND MESSAGE
	public ResponseEntity<Object> successResponseEntity(String message) {
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("status", "true");
		objectNode.put("message", message);
		return new ResponseEntity<>(objectNode, HttpStatus.OK);
	}

	// RESPONSE STATUS AND MESSAGE
	public ResponseEntity<Object> failResponseEntity(String message) {
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("status", "false");
		objectNode.put("message", message);
		return new ResponseEntity<>(objectNode, HttpStatus.FORBIDDEN);
	}
}
