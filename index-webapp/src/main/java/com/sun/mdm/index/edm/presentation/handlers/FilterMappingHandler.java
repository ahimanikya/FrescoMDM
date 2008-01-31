/*
 * FilterMappingHandler.java
 *
 * Created on September 13, 2007, 12:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.handlers;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Administrator
 */
public class FilterMappingHandler implements Filter {
    public static final String EXTENSION = "jsf";
    /** Creates a new instance of FilterMappingHandler */
    public FilterMappingHandler() {
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
          HttpServletRequest httpServletRequest = (HttpServletRequest) request;
          HttpServletResponse httpServletResponse = (HttpServletResponse)response;
          
          String uri = httpServletRequest.getRequestURI();
          if(uri.endsWith(".jsp")) {
              int uriLength = uri.length();
              String newAddress = uri.substring(0,uriLength-3)+ EXTENSION;
              httpServletResponse.sendRedirect(newAddress);
          } else {
              httpServletResponse.sendRedirect("login.jsf");
          }
    }
    public void destroy() {
    }
}
