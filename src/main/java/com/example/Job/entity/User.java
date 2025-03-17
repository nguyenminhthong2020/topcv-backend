package com.example.Job.entity;

import com.example.Job.constant.GenderEnum;
import com.example.Job.constant.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
//@PrimaryKeyJoinColumn(name = "user_id")
//@AllArgsConstructor
//@NoArgsConstructor
public class User extends Account{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private Set<JobApply> jobApplies = new HashSet<>();

    public User() {
        super();
        this.role = RoleEnum.USER;
    }

    // Constructor with fields
    public User(String email, String password, String name, Date birthday, GenderEnum gender) {
        super(email, password, RoleEnum.USER);
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }
}
