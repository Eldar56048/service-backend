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
import org.springframework.beans.factory.annotation.Value;
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


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.header.string}")
    public String HEADER_STRING;

    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;

    @Resource(name = "userService")
    private UserService userDetailsService;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        System.out.println(header);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.substring(7);
            System.out.println(authToken);
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            }   catch (ExpiredJwtException e) {
                logger.error("Срок действия токена истек");
                handleUnAuthorizedError(req, res, new AuthException("Срок действия токена истек", "auth/token-expired"));
            }   catch (IllegalArgumentException e) {
                logger.error("Произошла ошибка при получении имени пользователя из токена");
                handleUnAuthorizedError(req, res, new AuthException("Произошла ошибка при получении имени пользователя из токена", "fetch-user/error-token"));
            }  catch(SignatureException e){
                logger.error("Ошибка аутентификации. Имя пользователя или пароль недействительны");
                handleUnAuthorizedError(req, res, new AuthException("Ошибка аутентификации. Имя пользователя или пароль недействительны", "auth/invalid-username-and-password"));
            }  catch (MalformedJwtException e) {
                logger.error("Неверный формат пользовательского токена");
                handleUnAuthorizedError(req, res, new AuthException("Неверный формат пользовательского токена", "auth/invalid-token"));
            }
        } else {
            logger.error("Не удалось найти токен");
            handleUnAuthorizedError(req, res, new AuthException("Не удалось найти токен", "auth/not-token"));
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = userDetailsService.getByUsername(username);
            if (user.isEnabled() == false) {
                logger.error("Пользователь не активен");
                handleUnAuthorizedError(req, res, new AuthException("Ваш сервисный центр не активен", "service-center/not-active-token"));
            }
            else if (user.getServiceCenter().isEnabled()==false){
                logger.error("Сервисный центр не активен");
                handleUnAuthorizedError(req, res, new AuthException("Ваш аккаунт не активен", "user/not-active"));
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
            error = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), new Date(), "Smth went wrong", request.getRequestURI(), "auth/smth-went-wrong");
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
