package com.ddr.penerimaandocument.config;

import org.springframework.stereotype.Component;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter implements Filter {

    @Override
    public void doFilter(
      ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = (String) httpRequest.getSession().getAttribute("token");
        
        // if (token == null || token.isEmpty()) {
        //     System.out.println("GAADA TOKEN");
        //     // Jika token tidak ada atau kosong, kembalikan status tidak terotentikasi (401)
        //     httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //     return;
        // }

        chain.doFilter(request, response);
    }
}
