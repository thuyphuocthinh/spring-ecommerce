package com.tpt.tpt_ecom.repository;

import com.tpt.tpt_ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username) throws UsernameNotFoundException;
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
