package com.example.Job.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CompanyFollowId {
    private Long companyId;

    private Long userId;



}
