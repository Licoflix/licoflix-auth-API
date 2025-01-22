package com.auth.licoflix.core.service.userdetails;

import com.auth.licoflix.core.domain.dto.user.UserDetailsResponseImp;
import com.auth.licoflix.core.domain.model.user.User;
import com.auth.licoflix.core.domain.repository.UserRepository;
import com.auth.licoflix.core.domain.repository.UserSpecification;
import com.auth.licoflix.core.domain.validator.AuthenticationValidator;
import com.auth.licoflix.core.mapper.AuthenticationMapper;
import com.auth.licoflix.core.service.token.TokenService;
import com.auth.licoflix.utils.exception.ApplicationBusinessException;
import com.auth.licoflix.utils.response.DataListResponse;
import com.auth.licoflix.utils.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService, IUserDetailsService {

    private final UserRepository repository;
    private final TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }

    @Override
    public UserDetailsResponseImp loadUserByToken(String token, String timezone) throws UsernameNotFoundException, ApplicationBusinessException {
        String email = tokenService.extractUserEmail(token);
        User user = repository.getUserByEmail(email);

        AuthenticationValidator.validateIfUserIsDeleted(user);
        if (!tokenService.isTokenValid(token)) {
            token = tokenService.generateToken(user);
        }
        UserDetailsResponseImp response = AuthenticationMapper.userToUserDetails(token, user, timezone);
        fillAuditFields(response);

        return response;
    }

    @Override
    public User loadUserEntityByToken(String token, String timezone) throws UsernameNotFoundException, ApplicationBusinessException {
        String email = tokenService.extractUserEmail(token);
        User user = repository.getUserByEmail(email);

        AuthenticationValidator.validateIfUserIsDeleted(user);
        return user;
    }

    @Override
    public List<UserDetailsResponseImp> loadUserByIds(String token, String timezone, List<Long> ids) throws IOException {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<User> users = repository.findAllByIdIn(ids, sort);
        List<UserDetailsResponseImp> response = AuthenticationMapper.usersToUserDetails(users, timezone);
        fillUsersAuditFieldsInformation(response);
        return response;
    }

    @Override
    @Transactional
    public DataListResponse<UserDetailsResponseImp> list(String timezone, int page, int pageSize, String search, String token)
            throws UsernameNotFoundException {
        DataListResponse<UserDetailsResponseImp> response = new DataListResponse<>();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("deleted").nullsLast(),
                Sort.Order.desc("createdIn"), Sort.Order.desc("changedIn")));

        Page<User> users = repository.findAll(UserSpecification.containsTextInAttributes(search), pageable);
        List<UserDetailsResponseImp> list = fillUsersAuditFieldsInformation(AuthenticationMapper.usersToUserDetails(users.getContent(), timezone));

        response.setData(list);
        response.setTotalPages(users.getTotalPages());
        response.setTotalElements(users.getTotalElements());
        return response;
    }

    @Override
    public void fillAuditFields(UserDetailsResponseImp user) {
        if (user.getChangedBy() != null) {
            String changed = !user.getChangedBy().equals("-1") ?
                    repository.getReferenceById(Long.valueOf(user.getChangedBy())).getNickname() : "System";
            user.setChangedBy(changed);
        }
        if (user.getCreatedBy() != null) {
            String created = !user.getCreatedBy().equals("-1") ?
                    repository.getReferenceById(Long.valueOf(user.getCreatedBy())).getNickname() : "System";
            user.setCreatedBy(created);
        }
    }

    @Override
    @Transactional
    public DataResponse<UserDetailsResponseImp> delete(Long id, String token, String timezone, Boolean delete) {
        User user = repository.getReferenceById(id);
        user.setDeleted(delete);
        repository.save(user);

        DataResponse<UserDetailsResponseImp> dataResponse = new DataResponse<>();
        UserDetailsResponseImp response = AuthenticationMapper.userToUserDetails(token, user, timezone);
        fillAuditFields(response);

        dataResponse.setData(response);
        return dataResponse;
    }

    private List<UserDetailsResponseImp> fillUsersAuditFieldsInformation(List<UserDetailsResponseImp> list) {
        list.forEach(this::fillAuditFields);
        return list;
    }
}