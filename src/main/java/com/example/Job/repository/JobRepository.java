package com.example.Job.repository;

import com.example.Job.entity.Job;
import com.example.Job.models.dtos.GetJobResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {



    @Query("SELECT j FROM Job j JOIN FETCH j.company")
    Page<Job> findAllWithCompany(Pageable pageable);

    @Query("SELECT j FROM Job j JOIN FETCH j.company WHERE j.company.id = :companyId")
    Page<Job> findJobByCompanyId(@Param(value = "companyId") Long companyId, Pageable pageable);


    @Query(value = """
    SELECT
        j.id AS id,
        a.name AS companyName,
        c.img_url AS companyImg,
        c.id AS companyId,
        j.name AS name,
        j.year_of_experience AS yearOfExperience,
        j.salary_from AS salaryFrom,
        j.salary_to AS salaryTo,
        j.city AS city,
        ts_rank_cd(search_vector, plainto_tsquery('pg_catalog.simple', unaccent(:keyword))) as rank
    FROM jobs j
    JOIN companies c ON c.id = j.company_id
    JOIN accounts a ON c.id = a.id
    WHERE
        (:keyword IS NULL OR search_vector @@ plainto_tsquery('pg_catalog.simple', unaccent(:keyword)) )
        AND (:jobType IS NULL OR j.job_type = :jobType)
        AND (:industry IS NULL OR j.industry = :industry)
        AND (:level IS NULL OR j.level = :level)
        AND (j.year_of_experience BETWEEN :minExperience AND :maxExperience)
        AND (j.salary_from >= :minSalary OR j.salary_to <= :maxSalary )
        AND (:cities IS NULL OR j.city && string_to_array(:cities,',')::TEXT[]) 
    ORDER BY rank DESC
""", nativeQuery = true)
    Page<GetJobResponseDto> searchJobs(@Param("keyword") String keyword,
                                       @Param("jobType") String jobType,
                                       @Param("industry") String industry,
                                       @Param("level") String level,
                                       @Param("minExperience") Integer minExperience,
                                       @Param("maxExperience") Integer maxExperience,
                                       @Param("minSalary") Double minSalary,
                                       @Param("maxSalary") Double maxSalary,
                                       @Param("cities") String cities,
                                       Pageable pageable);


    @Query(value = """
        SELECT
            j.id AS id,
            a.name AS companyName,
            c.img_url AS companyImg,
            c.id AS companyId,
            j.name AS name,
            j.year_of_experience AS yearOfExperience,
            j.salary_from AS salaryFrom,
            j.salary_to AS salaryTo,
            j.city AS city
        FROM jobs j
        JOIN companies c ON c.id = j.company_id
        JOIN accounts a ON c.id = a.id
        WHERE j.id <> :jobId
        AND search_vector @@ replace(plainto_tsquery('pg_catalog.simple',
                            (SELECT unaccent(name) FROM jobs WHERE id = :jobId))::text,
                        '&',
                        '|'
                    )::tsquery
        LIMIT :limit
    """, nativeQuery = true)
    List<GetJobResponseDto> findRelatedJobsByKeyword(@Param("jobId") Long jobId, @Param("limit") int limit);
}
