package com.example.Job.repository;

import com.example.Job.constant.JobStatusEnum;
import com.example.Job.entity.Job;
import com.example.Job.models.dtos.GetJobResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    @Query("SELECT j FROM Job j JOIN FETCH j.company")
    Page<Job> findAllWithCompany(Pageable pageable);

    @Query("SELECT j FROM Job j JOIN FETCH j.company WHERE j.company.id = :companyId")
    Page<Job> findJobByCompanyId(@Param(value = "companyId") Long companyId, Pageable pageable);

    @Query("SELECT j FROM Job j JOIN FETCH j.company WHERE j.company.id = :companyId AND (:status IS NULL OR j.jobStatus = :status )")
    Page<Job> findJobByCompanyIdWithStatus(@Param(value = "companyId") Long companyId, @Param(value = "status") JobStatusEnum status, Pageable pageable);

    @Modifying
    @Query("""
            UPDATE Job j
            SET j.jobStatus = :jobStatus
            WHERE j.id = :jobId"""
    )
    void updateJobStatus(@Param("jobStatus") JobStatusEnum jobStatus, @Param("jobId") Long jobId);


    @EntityGraph(attributePaths = {"company" } )
    Page<Job> findAll(Specification<Job> spec, Pageable pageable);



//    @Query(value = """
//        SELECT
//            j.id AS id,
//            a.name AS companyName,
//            c.img_url AS companyImg,
//            c.id AS companyId,
//            j.name AS name,
//            j.year_of_experience AS yearOfExperience,
//            j.salary_from AS salaryFrom,
//            j.salary_to AS salaryTo,
//            j.city AS city,
//            j.created_at,
//            j.updated_at
//        FROM jobs j
//        JOIN companies c ON c.id = j.company_id
//        JOIN accounts a ON c.id = a.id
//        WHERE j.id <> :jobId
//        AND search_vector @@ replace(plainto_tsquery('pg_catalog.simple',
//                            (SELECT unaccent(name) FROM jobs WHERE id = :jobId))::text,
//                        '&',
//                        '|'
//                    )::tsquery
//        LIMIT :limit
//    """, nativeQuery = true)
//    List<GetJobResponseDto> findRelatedJobsByKeyword(@Param("jobId") Long jobId, @Param("limit") int limit);
}
