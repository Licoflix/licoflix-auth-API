package com.auth.licoflix.controller;

import com.auth.licoflix.core.domain.common.DomainReturnCode;
import com.auth.licoflix.core.domain.dto.user.UserDetailsResponseImp;
import com.auth.licoflix.core.service.userdetails.IUserDetailsService;
import com.auth.licoflix.utils.response.DataListResponse;
import com.auth.licoflix.utils.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "auth/user")
@CrossOrigin(origins = "${api.access.control.allow.origin}")
public class UserInformationController {
    private final IUserDetailsService service;

    @Operation(
            summary = "Get User Details",
            description = "Get User Details by Token"
    )
    @GetMapping(
            value = "/get"
    )
    public DataResponse<UserDetailsResponseImp> getByToken(
            @RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Timezone") String timezone
    ) throws Exception {
        DataResponse<UserDetailsResponseImp> response = new DataResponse<>();

        response.setData(service.loadUserByToken(token, timezone));
        response.setMessage(DomainReturnCode.SUCCESSFUL_OPERATION.getDesc());
        return response;
    }

    @Operation(
            summary = "Get User Details",
            description = "Get User Details by Ids"
    )
    @GetMapping(
            value = "/find"
    )
    public DataListResponse<UserDetailsResponseImp> findByIds(
            @RequestParam(name = "ids") List<Long> ids,
            @RequestHeader(name = "Timezone") String timezone,
            @RequestHeader(name = "Authorization") String token
    ) throws Exception {
        DataListResponse<UserDetailsResponseImp> response = new DataListResponse<>();
        response.setData(service.loadUserByIds(token, timezone, ids));
        response.setMessage(DomainReturnCode.SUCCESSFUL_OPERATION.getDesc());
        return response;
    }

    @Operation(
            summary = "List Users",
            description = "List Users by Filters"
    )
    @GetMapping(
            value = ""
    )
    public DataListResponse<UserDetailsResponseImp> list(
            @RequestHeader(name = "Timezone") String timezone,
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "search") String search
    ) throws Exception {
        return service.list(timezone, page, pageSize, search, token);
    }

    @Operation(
            summary = "Delete User",
            description = "Delete User by ID"
    )
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(
            @PathVariable(value = "id") Long id,
            @RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Timezone") String timezone
    ) throws Exception {
        service.delete(id, token, timezone);
    }
}