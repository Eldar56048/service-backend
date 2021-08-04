package com.crm.servicebackend.filter;

import com.crm.servicebackend.exception.ErrorDetails;
import com.crm.servicebackend.exception.domain.AuthException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.UserService;
import com.crm.servicebackend.utils.token.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.crm.servicebackend.constant.SecurityConstant.TOKEN_HEADER;
import static com.crm.servicebackend.constant.SecurityConstant.TOKEN_PREFIX;
import static com.crm.servicebackend.constant.model.auth.AuthResponseCode.*;
import static com.crm.servicebackend.constant.model.auth.AuthResponseCodeMessage.*;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource(name = "userService")
    private UserService userDetailsService;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(TOKEN_HEADER);
        String username = null;
        String authToken = null;
        System.out.println(header);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.substring(7);
            System.out.println(authToken);
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            }   catch (ExpiredJwtException e) {
                logger.error(TOKEN_EXPIRED_MESSAGE);
                handleUnAuthorizedError(req, res, new AuthException(TOKEN_EXPIRED_MESSAGE, TOKEN_EXPIRED_CODE));
            }   catch (IllegalArgumentException e) {
                logger.error(FETCH_USERNAME_FROM_TOKEN_ERROR_MESSAGE);
                handleUnAuthorizedError(req, res, new AuthException(FETCH_USERNAME_FROM_TOKEN_ERROR_MESSAGE, FETCH_USERNAME_FROM_TOKEN_ERROR_CODE));
            }  catch(SignatureException e){
                logger.error(INVALID_USERNAME_OR_PASSWORD_MESSAGE);
                handleUnAuthorizedError(req, res, new AuthException(INVALID_USERNAME_OR_PASSWORD_MESSAGE, INVALID_USERNAME_OR_PASSWORD_CODE));
            }  catch (MalformedJwtException e) {
                logger.error(INVALID_TOKEN_MESSAGE);
                handleUnAuthorizedError(req, res, new AuthException(INVALID_TOKEN_MESSAGE, INVALID_TOKEN_CODE));
            }
        } else {
            logger.error(NO_TOKEN_MESSAGE);
            handleUnAuthorizedError(req, res, new AuthException(NO_TOKEN_MESSAGE, NO_TOKEN_CODE));
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = userDetailsService.getByUsername(username);
            if (user.isEnabled() == false) {
                logger.error(SERVICE_CENTER_NOT_ACTIVE_MESSAGE);
                handleUnAuthorizedError(req, res, new AuthException(SERVICE_CENTER_NOT_ACTIVE_MESSAGE, SERVICE_CENTER_NOT_ACTIVE_CODE));
            }
            else if (user.getServiceCenter().isEnabled()==false){
                logger.error(ACCOUNT_NOT_ACTIVE_MESSAGE);
                handleUnAuthorizedError(req, res, new AuthException(ACCOUNT_NOT_ACTIVE_MESSAGE, ACCOUNT_NOT_ACTIVE_CODE));
            }
            else if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthenticationToken(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }

    public static void handleUnAuthorizedError(HttpServletRequest request, ServletResponse response, AuthException e)
    {
        ErrorDetails error = null;
        if(e!=null)
            error = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), new Date(), e.getMessage(), request.getRequestURI(), e.getCode());
        else
            error = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), new Date(), SOMETHING_WENT_WRONG_MESSAGE, request.getRequestURI(), SOMETHING_WENT_WRONG_CODE);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            httpResponse.getWriter().println(convertToJSON(error));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static String convertToJSON(Object inputObj) {
        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = null;
        try {
            orderJson = objectMapper.writeValueAsString(inputObj);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return orderJson;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathRequestMatcher("/api/v1/client/**").matches(request) || new AntPathRequestMatcher("/api/v1/auth").matches(request);
    }

}
