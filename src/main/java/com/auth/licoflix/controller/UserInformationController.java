package com.auth.licoflix.controller;

import com.auth.licoflix.core.domain.common.DomainReturnCode;
import com.auth.licoflix.core.domain.dto.user.UserDetailsResponseImp;
import com.auth.licoflix.core.service.userdetails.IUserDetailsService;
import com.auth.licoflix.utils.response.DataListResponse;
import com.auth.licoflix.utils.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "auth/user")
@CrossOrigin(origins = "${api.access.control.allow.origin}")
public class UserInformationController {
    private static final String STARTED = " - Started";
    private static final String FINISHED = " - Finished";
    private static final Logger logger = LoggerFactory.getLogger(UserInformationController.class);

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
        logger.info("Get By Token method" + STARTED);
        logger.debug("Get By Token method, Params: Timezone: {}, Token: {}", timezone, token);

        DataResponse<UserDetailsResponseImp> response = new DataResponse<>();

        response.setData(service.loadUserByToken(token, timezone));
        response.setMessage(DomainReturnCode.SUCCESSFUL_OPERATION.getDesc());

        logger.debug("Get By Token method, Response: {}", response);
        logger.info("Get By Token method" + FINISHED);
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
        logger.info("Find By Ids method" + STARTED);
        logger.debug("Find By Ids method, Params: IDs: {}, Timezone: {}, Token: {}", ids, timezone, token);
        DataListResponse<UserDetailsResponseImp> response = new DataListResponse<>();

        response.setData(service.loadUserByIds(token, timezone, ids));
        response.setMessage(DomainReturnCode.SUCCESSFUL_OPERATION.getDesc());

        logger.debug("Find By Ids method, Response: {}", response.getData());
        logger.info("Find By Ids method" + FINISHED);
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
        logger.info("List by Filters method" + STARTED);
        logger.debug("List by Filters method, Params: Page: {}, Page Size: {}, Search: {}, Timezone: {}, Token: {}", page,
                pageSize, search, timezone, token);

        DataListResponse<UserDetailsResponseImp> response = service.list(timezone, page, pageSize, search, token);

        logger.debug("List by Filters method, Response: {}", response.getData());
        logger.info("List by Filters method" + FINISHED);
        return response;
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
        logger.info("Delete method" + STARTED);
        logger.debug("Delete method, Params: ID: {}, Timezone: {}, Token: {}", id, timezone, token);

        service.delete(id, token, timezone);

        logger.info("Delete method" + FINISHED);
    }
}