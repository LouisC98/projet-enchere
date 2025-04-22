package fr.eni.encheres.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SessionExpirationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        
        if ((session == null || session.isNew()) && 
            !httpRequest.getRequestURI().startsWith("/login") && 
            !httpRequest.getRequestURI().startsWith("/register") &&
            !httpRequest.getRequestURI().startsWith("/change-password") &&
            !httpRequest.getRequestURI().startsWith("/css") &&
            !httpRequest.getRequestURI().startsWith("/js") &&
            !httpRequest.getRequestURI().startsWith("/images")) {
            
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?expired");
            return;
        }
        
        chain.doFilter(request, response);
    }
}