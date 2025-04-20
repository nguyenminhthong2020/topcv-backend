package com.example.Job.specifications;

import com.example.Job.constant.IndustryEnum;
import com.example.Job.constant.JobTypeEnum;
import com.example.Job.constant.LevelEnum;
import com.example.Job.entity.Job;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;



public class JobSpecifications {


    public static Specification<Job> hasSimilarKeywordWithTitle(String title){
        return (root, query, cb) -> {
            if(title == null || title.isEmpty()) return null;



            return cb.isTrue(
                    cb.function("fts_or_query", Boolean.class,
                            root.get("searchVector"),
                            cb.literal(title)
                    )
            );
        };
    }
    public static Specification<Job> hasKeyword(String keyword){
        return (root, query, cb) -> {
            if(keyword == null || keyword.isEmpty()) return null;

            // Get searchVector column
//            Expression<String> searchVector = root.get("searchVector");
//            // Build unaccented tsquery from keyword: plainto_tsquery('simple', unaccent(:keyword))
//
//            Expression<String> keywordQuery = cb.function("plainto_tsquery",  // name of function
//                    String.class, // return type
//                    // arguments
//                    cb.literal("simple"),  // first argument
//                    cb.function("unaccent", String.class, cb.literal(keyword)) // second argument
//            );



            return cb.isTrue(
              cb.function("fts_match", Boolean.class, root.get("searchVector"), cb.literal(keyword))
            );
        };
    }

//    public static Specification<Job> orderByRank(String keyword){
//        return (root, query, cb) -> {
//            if(keyword == null || keyword.isEmpty()) return null;
//
//            // Get searchVector column
////            Expression<String> searchVector = root.get("searchVector");
////            // Build unaccented tsquery from keyword: plainto_tsquery('simple', unaccent(:keyword))
////
////            Expression<String> keywordQuery = cb.function("plainto_tsquery",  // name of function
////                    String.class, // return type
////                    // arguments
////                    cb.literal("simple"),  // first argument
////                    cb.function("unaccent", String.class, cb.literal(keyword)) // second argument
////            );
//
//            Expression<Float> rank = cb.function(
//                    "fts_rank",
//                    Float.class,
//                    root.get("searchVector"),
//                    cb.literal(keyword)
//            );
//            Order rankOrder = cb.desc(rank);
////            orders.add(rankOrder);
//
//            return cb.conjunction();
//        };
//    }
    public static Specification<Job> hasIndustry(IndustryEnum industry){
        return (root, query, cb) -> {
            if(industry == null ) return null;

            return cb.equal(root.get("industry"), industry);
        };
    }

    public static Specification<Job> hasJobType(JobTypeEnum jobType){
        return (root, query, cb) -> {
            if(jobType == null) return null;
            return cb.equal(root.get("jobType"), jobType);
        };
    }

    public static Specification<Job> hasLevel(LevelEnum level){
        return (root, query, cb) -> {
            if(level == null) return null;

            return cb.equal(root.get("level"), level);
        };
    }

    public static Specification<Job> hasExperienceBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (min != null) predicates.add(cb.greaterThanOrEqualTo(root.get("yearOfExperience"), min));
            if (max != null) predicates.add(cb.lessThanOrEqualTo(root.get("yearOfExperience"), max));
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Job> hasSalaryBetween(Double min, Double max) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (min != null) predicates.add(cb.greaterThanOrEqualTo(root.get("salaryFrom"), min));
            if (max != null) predicates.add(cb.lessThanOrEqualTo(root.get("salaryTo"), max));
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Job> hasCityIn(List<String> cities){
        return (root, query, cb) -> {
            if(cities == null || cities.isEmpty()) return null;

            return cb.in(root.get("city")).value(cities);
        };
    }

}
