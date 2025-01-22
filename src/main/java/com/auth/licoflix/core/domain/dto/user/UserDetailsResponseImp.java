package com.auth.licoflix.core.domain.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseImp {
    private Long id;
    private String name;
    private String email;
    private String token;
    private byte[] avatar;
    private boolean admin;
    private boolean deleted;
    private String nickname;
    private String createdIn;
    private String changedIn;
    private String createdBy;
    private String changedBy;
    private boolean authenticated;

    @Override
    public String toString() {
        return "Id: " + id + ", " +
                "Name: " + name + ", " +
                "Email: " + email + ", " +
                "Nickname: " + nickname + ", " +
                "Token: " + token + ", " +
                "Authenticated: " + authenticated + ", " +
                "CreatedIn: " + createdIn + ", " +
                "ChangedIn: " + changedIn + ", " +
                "CreatedBy: " + createdBy + ", " +
                "ChangedBy: " + changedBy + ", " +
                "Deleted: " + deleted;
    }
}