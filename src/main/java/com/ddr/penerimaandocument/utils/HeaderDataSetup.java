package com.ddr.penerimaandocument.utils;

import java.util.LinkedHashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HeaderDataSetup {

	private HttpEntity<String> httpEntityPost;
	private HttpEntity<String> httpEntity;

	public HeaderDataSetup(LinkedHashMap<String, Object> classUsed) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity httpEntityPost = new HttpEntity<>(classUsed, headers);

		this.httpEntityPost = httpEntityPost;
	}
	
	public HeaderDataSetup() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		this.httpEntity = new HttpEntity<String>(headers);
	}

	public HttpEntity<String> getHttpEntityPost() {
		return httpEntityPost;
	}

	public void setHttpEntityPost(HttpEntity<String> httpEntityPost) {
		this.httpEntityPost = httpEntityPost;
	}
	
	public HttpEntity<String> getHttpEntity() {
		return httpEntity;
	}

	public void setHttpEntity(HttpEntity<String> httpEntity) {
		this.httpEntity = httpEntity;
	}
	
	
}