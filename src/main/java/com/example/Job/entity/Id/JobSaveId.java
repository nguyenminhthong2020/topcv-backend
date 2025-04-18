package com.example.Job.entity.Id;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor

public class JobSaveId {
    private Long userId;

    private Long jobId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobSaveId jobSaveId = (JobSaveId) o;
        return Objects.equals(userId, jobSaveId.userId) && Objects.equals(jobId, jobSaveId.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, jobId);
    }
}
