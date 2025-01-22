package com.auth.licoflix.controller;

import com.auth.licoflix.core.domain.common.DomainReturnCode;
import com.auth.licoflix.core.domain.dto.login.LoginRequest;
import com.auth.licoflix.core.domain.dto.register.RegisterRequest;
import com.auth.licoflix.core.domain.dto.user.UserDetailsResponseImp;
import com.auth.licoflix.core.service.auth.IAuthenticationService;
import com.auth.licoflix.utils.exception.ApplicationBusinessException;
import com.auth.licoflix.utils.request.DataRequest;
import com.auth.licoflix.utils.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "${api.access.control.allow.origin}")
public class AuthenticationController {
    private static final String STARTED = " - Started";
    private static final String FINISHED = " - Finished";
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final IAuthenticationService authenticationService;

    @Operation(
            summary = "Login",
            description = "User Login"
    )
    @PostMapping(
            value = "/login",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<UserDetailsResponseImp> login(
            @RequestBody @Valid LoginRequest bodyRequest,
            @RequestHeader(name = "Timezone") String timezone) throws ApplicationBusinessException {
        logger.info("Login method" + STARTED);
        logger.debug("Login method, Params: Request: {}, Timezone: {}", bodyRequest, timezone);

        DataResponse<UserDetailsResponseImp> response = new DataResponse<>();
        DataRequest<LoginRequest> request = new DataRequest<>(bodyRequest);

        response.setData(authenticationService.login(request, timezone));
        response.setMessage(DomainReturnCode.SUCCESSFUL_OPERATION.getDesc());

        logger.debug("Login method, Response: {}", response);
        logger.info("Login method" + FINISHED);
        return response;
    }

    @Operation(
            summary = "Register",
            description = "User Register"
    )
    @PostMapping(
            value = "/register",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<UserDetailsResponseImp> register(
            @RequestBody RegisterRequest bodyRequest,
            @RequestHeader(name = "Timezone") String timezone,
            @RequestHeader(name = "Authorization", required = false) String token) throws Exception {
        logger.info("Register method" + STARTED);
        logger.debug("Register method, Params: Request: {}, Timezone: {}", bodyRequest, timezone);

        DataResponse<UserDetailsResponseImp> response = new DataResponse<>();
        DataRequest<RegisterRequest> request = new DataRequest<>(bodyRequest);

        response.setData(authenticationService.register(request, timezone, token));
        response.setMessage(DomainReturnCode.SUCCESSFUL_OPERATION.getDesc());

        logger.debug("Register method, Response: {}", response);
        logger.info("Register method" + FINISHED);
        return response;
    }

    @Operation(
            summary = "Edit",
            description = "Edit User"
    )
    @PutMapping(
            value = "/edit",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<UserDetailsResponseImp> edit(
            @RequestBody RegisterRequest bodyRequest,
            @RequestHeader(name = "Timezone") String timezone,
            @RequestHeader(name = "Authorization", required = false) String token) throws ApplicationBusinessException, IOException {
        logger.info("Edit method" + STARTED);
        logger.debug("Edit method, Params: Request: {}, Timezone: {}, Token: {}", bodyRequest, timezone, token);

        DataResponse<UserDetailsResponseImp> response = new DataResponse<>();
        DataRequest<RegisterRequest> request = new DataRequest<>(bodyRequest);

        response.setData(authenticationService.edit(request, timezone, token));
        response.setMessage(DomainReturnCode.SUCCESSFUL_OPERATION.getDesc());

        logger.debug("Edit method, Response: {}", response);
        logger.info("Edit method" + FINISHED);
        return response;
    }
}