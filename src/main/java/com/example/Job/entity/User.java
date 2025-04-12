package com.example.Job.entity;

import com.example.Job.constant.GenderEnum;
import com.example.Job.constant.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

public class User extends Account{

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

//    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<JobApply> jobApplies = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Job_Save",
            joinColumns = @JoinColumn(name = "user_id") ,
            inverseJoinColumns =  @JoinColumn(name = "job_id")
    )

    Set<Job> savedJobs;

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
