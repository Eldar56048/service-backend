package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.login.LoginDtoRequest;
import com.crm.servicebackend.dto.responseDto.login.LoginDtoResponse;
import com.crm.servicebackend.exception.domain.AuthException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.model.domain.AuthToken;
import com.crm.servicebackend.service.UserService;
import com.crm.servicebackend.utils.facade.UserFacade;
import com.crm.servicebackend.utils.token.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> generateToken(@RequestBody LoginDtoRequest loginUser) throws AuthenticationException {
        User user = (User) userService.loadUserByUsername(loginUser.getUsername());
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            loginUser.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AuthException("Неправильное имя пользователя или пароль", "auth/invalid-username-or-password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new LoginDtoResponse(new AuthToken(token), UserFacade.modelToDtoResponse(user)));
    }
}
