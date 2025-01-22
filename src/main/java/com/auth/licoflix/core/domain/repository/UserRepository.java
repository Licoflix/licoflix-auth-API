package com.auth.licoflix.core.domain.repository;

import com.auth.licoflix.core.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);

    Optional<User> findUserByEmail(String email);

    @Query("select u from User u where u.email = :email")
    User getUserByEmail(String email);

    @Query("select count(u.id) from User u where u.nickname = :nickname")
    Integer findByNickname(String nickname);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    List<User> findAllByIdIn(List<Long> ids, Sort sort);

    @Query("select u from User u where u.name = :name and u.id != :id")
    Optional<User> findByNameAndIdNotEquals(String name, Long id);

    @Query("select count(u.id) from User u where u.name = :name")
    Integer findByName(String name);
}