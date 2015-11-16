package com.chat.server.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created on 12.11.2015.
 */
@Component("unauthorizedEntryPoint")
public final class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        System.out.println(" *** UnauthorizedEntryPoint.commence: " + request.getRequestURI());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
