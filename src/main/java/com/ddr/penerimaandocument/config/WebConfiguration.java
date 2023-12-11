package com.ddr.penerimaandocument.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebConfiguration implements WebMvcConfigurer {

	@Value("${filestorage}")
	private String FILESTORAGE;
	
	@Value("${filekeyp12}")
	private String FILE_KEY_P12;
    
    @Bean(name = "restTemplateHttp")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}