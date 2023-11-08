package org.kyi.solution.repository;

import org.kyi.solution.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserName(String userName);
    Optional<User> findUserByEmail(String email);
}

