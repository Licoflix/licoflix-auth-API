package com.auth.licoflix.core.service.userdetails;

import com.auth.licoflix.core.domain.dto.user.UserDetailsResponseImp;
import com.auth.licoflix.core.domain.model.user.User;
import com.auth.licoflix.utils.exception.ApplicationBusinessException;
import com.auth.licoflix.utils.response.DataListResponse;
import com.auth.licoflix.utils.response.DataResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;

public interface IUserDetailsService {
    void fillAuditFields(UserDetailsResponseImp user);

    List<UserDetailsResponseImp> loadUserByIds(String token, String timezone, List<Long> ids) throws IOException;

    UserDetailsResponseImp loadUserByToken(String token, String timezone) throws UsernameNotFoundException, ApplicationBusinessException, IOException;

    User loadUserEntityByToken(String token, String timezone) throws UsernameNotFoundException, ApplicationBusinessException;

    DataResponse<UserDetailsResponseImp> delete(Long id, String token, String timezone, Boolean delete) throws ApplicationBusinessException, IOException;

    DataListResponse<UserDetailsResponseImp> list(String timezone, int page, int pageSize, String search, String token) throws UsernameNotFoundException, ApplicationBusinessException, IOException;
}