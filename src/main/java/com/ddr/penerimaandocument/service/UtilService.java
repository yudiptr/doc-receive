package com.ddr.penerimaandocument.service;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ddr.penerimaandocument.dto.LoginResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.ddr.penerimaandocument.utils.DisableSSLUtils;
import com.ddr.penerimaandocument.utils.HeaderDataSetup;

@Service
public class UtilService {
	
	private final RestTemplate restTemplate;
	
	@Value("${host.and.port.hris}")
	private String HOST_AND_PORT_HRIS;
		
	@Value("${openendpoint.getauth}")
	private String GET_AUTH;
	
	@Value("${openendpoint.getallcompany}")
	private String GET_ALL_COMPANY;

	@Autowired
	public UtilService(@Qualifier("restTemplateHttp")RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	

	public LoginResponseDTO authenticate(LinkedHashMap<String, Object> req){

		HeaderDataSetup hds = new HeaderDataSetup(req);
		HttpEntity<String> entity = hds.getHttpEntityPost();
		String postUrl = "https://119.28.20.4:8443/hris/dologinservice";
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		DisableSSLUtils.turnOffCertificate();
		ResponseEntity<LoginResponseDTO> branchs = restTemplate.exchange(postUrl, HttpMethod.POST, entity, LoginResponseDTO.class);
		LoginResponseDTO res = branchs.getBody();
		return res;
	}
}
