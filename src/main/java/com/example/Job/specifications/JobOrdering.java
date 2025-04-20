package com.example.Job.specifications;

import com.example.Job.entity.Job;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class JobOrdering {
    private final List<Order> orders = new ArrayList<>();

    public void addSortBy(Root<Job> root, CriteriaBuilder cb, String sortBy, boolean isAscending) {
        if (sortBy != null && !sortBy.isEmpty()) {
            Order order = isAscending ? cb.asc(root.get(sortBy)) : cb.desc(root.get(sortBy));
            orders.add(order);
        }
    }

    public void addRankOrder(Root<Job> root, CriteriaBuilder cb, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            Expression<Float> rank = cb.function(
                    "fts_rank",
                    Float.class,
                    root.get("searchVector"),
                    cb.literal(keyword)
            );

            orders.add(cb.desc(rank));
        }
    }

    public void apply(CriteriaQuery<?> query) {

        if (!orders.isEmpty()) {
            query.orderBy(orders);
        }
    }
}
