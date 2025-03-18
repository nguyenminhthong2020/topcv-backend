package com.example.Job.repository;

import com.example.Job.entity.Account;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);


    Optional<Account> findByEmail(String email);
}
