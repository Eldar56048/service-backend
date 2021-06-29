package com.crm.servicebackend.dto.responseDto.login;

import com.crm.servicebackend.dto.responseDto.user.UserDtoResponse;
import com.crm.servicebackend.model.domain.AuthToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDtoResponse {
    private AuthToken token;
    private UserDtoResponse user;
}

