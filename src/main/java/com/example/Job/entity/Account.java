package com.example.Job.entity;

import com.example.Job.constant.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter

public abstract class Account {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "account_seq")
//    @SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "name", nullable = true)
    protected String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false, updatable = false) //  Prevent duplicate mapping
    protected RoleEnum role;
    protected Account() {}


    protected Account(String email, String password, RoleEnum roleEnum) {
        this.email = email;
        this.password = password;
        this.role = roleEnum;
    }
}
