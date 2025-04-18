package com.example.Job.entity;

import com.example.Job.entity.Id.JobSaveId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "job_save")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSave {
    @EmbeddedId
    private JobSaveId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobId") // Maps the jobId in JobSaveId
    @JoinColumn(name = "job_id")
    private Job job;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobSave jobSave = (JobSave) o;
        return Objects.equals(id, jobSave.id) && Objects.equals(user, jobSave.user) && Objects.equals(job, jobSave.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, job);
    }
}
