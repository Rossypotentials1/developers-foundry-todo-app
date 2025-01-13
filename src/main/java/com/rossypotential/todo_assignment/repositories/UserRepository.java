package com.rossypotential.todo_assignment.repositories;

import com.rossypotential.todo_assignment.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByVerificationCode(String verificationCode);

    Optional<AppUser> findByEmail(String email);

    Boolean existsByEmail(String email);

    Page<AppUser> findAll(Pageable pageable);


}
