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

import static com.crm.servicebackend.constant.model.auth.AuthResponseCode.INVALID_USERNAME_OR_PASSWORD_CODE;
import static com.crm.servicebackend.constant.model.auth.AuthResponseCode.SERVICE_CENTER_NOT_ACTIVE_CODE;
import static com.crm.servicebackend.constant.model.auth.AuthResponseCodeMessage.INVALID_USERNAME_OR_PASSWORD_MESSAGE;
import static com.crm.servicebackend.constant.model.auth.AuthResponseCodeMessage.SERVICE_CENTER_NOT_ACTIVE_MESSAGE;

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
        if (user.getServiceCenter().isEnabled()==false){
            throw new AuthException(SERVICE_CENTER_NOT_ACTIVE_MESSAGE, SERVICE_CENTER_NOT_ACTIVE_CODE);
        }
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            loginUser.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AuthException(INVALID_USERNAME_OR_PASSWORD_MESSAGE, INVALID_USERNAME_OR_PASSWORD_CODE);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new LoginDtoResponse(new AuthToken(token), UserFacade.modelToDtoResponse(user)));
    }
}
