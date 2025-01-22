package com.auth.licoflix.core.domain.dto.register;

public record RegisterRequest(String name, String nickname, String email, String password, Long role, String avatar) {
    @Override
    public String toString() {
        return "Name: " + name + ", " +
                "Nickname: " + nickname + ", " +
                "Email: " + email + ", " +
                "Password: " + password + ", " +
                "Role: " + role + ", " +
                "Avatar: " + avatar;
    }
}