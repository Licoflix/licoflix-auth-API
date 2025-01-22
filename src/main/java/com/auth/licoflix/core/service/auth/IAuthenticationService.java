package com.auth.licoflix.core.service.auth;

import com.auth.licoflix.core.domain.dto.login.LoginRequest;
import com.auth.licoflix.core.domain.dto.register.RegisterRequest;
import com.auth.licoflix.core.domain.dto.user.UserDetailsResponseImp;
import com.auth.licoflix.utils.exception.ApplicationBusinessException;
import com.auth.licoflix.utils.request.DataRequest;

import java.io.IOException;

public interface IAuthenticationService {
    default UserDetailsResponseImp login(DataRequest<LoginRequest> request, String locale) throws ApplicationBusinessException {
        return null;
    }

    UserDetailsResponseImp register(DataRequest<RegisterRequest> request, String locale, String token) throws ApplicationBusinessException, IOException;

    UserDetailsResponseImp edit(DataRequest<RegisterRequest> request, String timezone, String token) throws ApplicationBusinessException, IOException;
}