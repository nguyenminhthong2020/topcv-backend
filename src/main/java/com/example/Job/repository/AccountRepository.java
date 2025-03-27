package com.example.Job.repository;

import com.example.Job.entity.Account;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a WHERE LOWER(a.name) LIKE LOWER(CONCAT(:name, '%'))")
    List<Account> findAccountsByName(@Param("name") String name);

    Optional<Account> findByEmail(String email);
}
