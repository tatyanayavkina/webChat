package com.chat.server.oauth2.service;

import com.chat.server.oauth2.domain.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.jboss.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created on 10.11.2015.
 */
@Service
public class OAuth2Filter implements Filter {
    protected final Logger logger = Logger.getLogger(getClass());
    public final static String ACCESS_TOKEN_HEADER = "AccessToken";
//    public final static String ORIGIN_HEADER = "Origin";

    @Autowired
    @Lazy
    private AccessService service;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do Nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String accessToken = httpServletRequest.getHeader(ACCESS_TOKEN_HEADER);
        if (accessToken != null && ! accessToken.isEmpty()) {
            service.checkToken(accessToken);
        }

        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {
        // do Nothing
    }
}
