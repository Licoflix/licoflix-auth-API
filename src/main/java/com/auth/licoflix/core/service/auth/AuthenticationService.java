package com.auth.licoflix.core.service.auth;

import com.auth.licoflix.core.domain.common.DomainReturnCode;
import com.auth.licoflix.core.domain.dto.login.LoginRequest;
import com.auth.licoflix.core.domain.dto.register.RegisterRequest;
import com.auth.licoflix.core.domain.dto.user.UserDetailsResponseImp;
import com.auth.licoflix.core.domain.model.user.User;
import com.auth.licoflix.core.domain.repository.UserRepository;
import com.auth.licoflix.core.domain.validator.AuthenticationValidator;
import com.auth.licoflix.core.mapper.AuthenticationMapper;
import com.auth.licoflix.core.service.token.ITokenService;
import com.auth.licoflix.core.service.userdetails.IUserDetailsService;
import com.auth.licoflix.utils.exception.ApplicationBusinessException;
import com.auth.licoflix.utils.request.DataRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final ITokenService tokenService;
    private final UserRepository userRepository;
    private static final String BEARER = "Bearer ";
    private final IUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetailsResponseImp login(DataRequest<LoginRequest> request, String timezone) throws ApplicationBusinessException {
        UserDetailsResponseImp response;

        try {
            var credentials = new UsernamePasswordAuthenticationToken(request.getData().email(), request.getData().password());
            Authentication auth = authenticationManager.authenticate(credentials);
            String token = BEARER;
            token += tokenService.generateToken((User) auth.getPrincipal());
            response = AuthenticationMapper.authToUserDetails(auth, token, timezone);

        } catch (Exception e) {
            if (e.getMessage().equals("Conta bloqueada"))
                throw new ApplicationBusinessException(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        DomainReturnCode.DELETED_USER.toString(),
                        DomainReturnCode.DELETED_USER.getDesc());
            throw new ApplicationBusinessException(
                    HttpServletResponse.SC_BAD_REQUEST,
                    DomainReturnCode.FAILED_LOGIN.toString(),
                    DomainReturnCode.FAILED_LOGIN.getDesc());
        }
        userDetailsService.fillAuditFields(response);
        return response;
    }

    @Override
    @Transactional
    public UserDetailsResponseImp register(DataRequest<RegisterRequest> request, String timezone, String token) throws ApplicationBusinessException {
        if (token != null)
            AuthenticationValidator.validateIfUserExists(userRepository.findUserByEmail(request.getData().email()));
        AuthenticationValidator.validateIfSameNameExists(userRepository.findByName(request.getData().name()));

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.getData().password());
        User user = User.builder().name(request.getData().name()).email(request.getData().email())
                .password(encryptedPassword).nickname(request.getData().nickname()).build();
        user.setCreatedBy(token == null ? -1L : tokenService.extractUserId(token));
        userRepository.save(user);

        UserDetailsResponseImp response = AuthenticationMapper.userToUserDetails(tokenService.generateToken(user), user, timezone);
        userDetailsService.fillAuditFields(response);
        return response;
    }

    @Override
    public UserDetailsResponseImp edit(DataRequest<RegisterRequest> request, String timezone, String token)
            throws ApplicationBusinessException, IOException {

        User user = userDetailsService.loadUserEntityByToken(token, request.getLocale());

        AuthenticationValidator.validateIfUserExists(userRepository.findByNameAndIdNotEquals(request.getData().name(), user.getId()));

        if (!user.getName().equals(request.getData().name()))
            AuthenticationValidator.validateIfSameNameExists(userRepository.findByName(request.getData().name()));
        if (!user.getEmail().equals(request.getData().email()))
            AuthenticationValidator.validateIfUserExists(userRepository.findUserByEmail(request.getData().email()));

        AuthenticationMapper.edit(user, request.getData(), tokenService.extractUserId(token));
        userRepository.save(user);

        String newToken = tokenService.generateToken(user);
        return AuthenticationMapper.userToUserDetails(newToken, user, timezone);
    }
}
