package ru.interview.repositories;

import org.springframework.data.jpa.domain.Specification;
import ru.interview.model.Currency;
import ru.interview.model.History;

import java.util.Date;

public class HistorySpecifications {

    public static Specification<History> fromThisDate(Date date) {
        return (Specification<History>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), date);
        };
    }

    public static Specification<History> fromTillThisDate(Date date) {
        return (Specification<History>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("date"), date);
        };
    }
}
