package com.ddr.penerimaandocument.config;

import org.springframework.beans.factory.annotation.Autowired;
import com.ddr.penerimaandocument.service.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class TokenFilter implements Filter {
  
  @Autowired
  private JwtService jwtService;
  
  public TokenFilter(){
    jwtService = new JwtService();
  }

  @Override
  public void doFilter(
    ServletRequest request, 
    ServletResponse response, 
    FilterChain chain) throws IOException, ServletException {
      
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;

      HttpSession session = httpRequest.getSession();
      String token = (String) session.getAttribute("token");

      System.out.println("token");
      System.out.println(token);
      String uri = httpRequest.getRequestURI();

      if (uri.equals("/login") || uri.startsWith("/login/")) {
          chain.doFilter(request, response);
      }
      else {
        if (token == null || token.isEmpty() || jwtService.isTokenExpired(token)) {
          httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          httpResponse.sendRedirect("/login");
          return;
        }

        chain.doFilter(request, response);
      }
  }
}
