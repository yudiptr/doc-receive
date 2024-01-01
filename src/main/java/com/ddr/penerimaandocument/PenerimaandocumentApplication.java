package com.ddr.penerimaandocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import jakarta.servlet.Filter;
import com.ddr.penerimaandocument.config.TokenFilter;

@SpringBootApplication
public class PenerimaandocumentApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PenerimaandocumentApplication.class, args);
	}

	@Bean
    public FilterRegistrationBean<Filter> filterToken() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenFilter());
        registrationBean.addUrlPatterns("/*"); // Atur pola URL yang ingin Anda filter
        return registrationBean;
    }

}
