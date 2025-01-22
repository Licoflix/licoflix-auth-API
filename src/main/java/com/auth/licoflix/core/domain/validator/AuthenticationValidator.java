package com.auth.licoflix.core.domain.validator;

import com.auth.licoflix.core.domain.common.DomainReturnCode;
import com.auth.licoflix.core.domain.model.user.User;
import com.auth.licoflix.utils.exception.ApplicationBusinessException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@NoArgsConstructor
public class AuthenticationValidator {

    public static void validateIfUserExists(Optional<User> user) throws ApplicationBusinessException {
        if (user.isPresent()) {
            throw new ApplicationBusinessException(
                    HttpServletResponse.SC_BAD_REQUEST,
                    DomainReturnCode.USER_EXISTS.toString(),
                    DomainReturnCode.USER_EXISTS.getDesc()
            );
        }
    }


    public static void validateIfSameNameExists(Integer same) throws ApplicationBusinessException {
        if (same > 0) {
            throw new ApplicationBusinessException(
                    HttpServletResponse.SC_BAD_REQUEST,
                    DomainReturnCode.NAME_EXISTS.toString(),
                    DomainReturnCode.NAME_EXISTS.getDesc()
            );
        }
    }

    public static void validateIfUserIsDeleted(User user) throws ApplicationBusinessException {
        if (user.isDeleted())
            throw new ApplicationBusinessException(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    DomainReturnCode.DELETED_USER.toString(),
                    DomainReturnCode.DELETED_USER.getDesc());
    }
}