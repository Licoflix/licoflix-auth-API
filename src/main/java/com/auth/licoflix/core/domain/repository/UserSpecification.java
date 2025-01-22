package com.auth.licoflix.core.domain.repository;

import com.auth.licoflix.core.domain.model.user.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class UserSpecification {

    public static Specification<User> containsTextInAttributes(String text) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addGlobalSearchConditions(predicates, builder, root, text);
            addDateConditions(predicates, builder, root, text);
            return builder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addGlobalSearchConditions(List<Predicate> predicates, CriteriaBuilder builder, Root<User> root,
                                                  String text) {
        boolean onlyDeleted = text.equalsIgnoreCase("onlyDeleted");
        if (!onlyDeleted) {
            String likePattern = "%" + text.toLowerCase() + "%";
            predicates.add(builder.like(builder.lower(root.get("email")), likePattern));
            predicates.add(builder.like(builder.lower(root.get("nickname")), likePattern));
        }
    }

    private static <T> void addDateConditions(List<Predicate> predicates, CriteriaBuilder builder, Root<T> root, String text) {
        List<Predicate> datePredicates = new ArrayList<>();
        try {
            getPredicateForFullDate(text, builder, root, "dd/MM/yyyy", datePredicates);
            getPredicateForFullDate(text, builder, root, "MM/dd/yyyy", datePredicates);
        } catch (Exception ignored) {
            // Ignore parse errors
        }

        try {
            int year = Integer.parseInt(text);
            LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
            LocalDateTime endOfYear = LocalDate.of(year, 12, 31).atTime(23, 59, 59);

            Predicate createdInPredicate = builder.between(root.get("createdIn"), startOfYear, endOfYear);
            Predicate changedInPredicate = builder.or(
                    builder.isNull(root.get("changedIn")),
                    builder.between(root.get("changedIn"), startOfYear, endOfYear)
            );

            predicates.add(builder.and(createdInPredicate, changedInPredicate));
        } catch (NumberFormatException ignored) {
            // Ignore if not a year
        }

        predicates.addAll(datePredicates);
    }

    private static <T> void getPredicateForFullDate(String text, CriteriaBuilder builder, Root<T> root, String pattern, List<Predicate> predicates) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime fullDate = LocalDate.parse(text, formatter).atStartOfDay();

            Expression<LocalDateTime> truncatedCreatedIn = builder.function(
                    "DATE_TRUNC", LocalDateTime.class,
                    builder.literal("day"), root.get("createdIn")
            );
            Predicate createdInPredicate = builder.equal(truncatedCreatedIn, fullDate);

            Predicate changedInPredicate = builder.or(
                    builder.equal(truncatedCreatedIn, fullDate),
                    builder.isNull(root.get("changedIn"))
            );

            predicates.add(builder.and(createdInPredicate, changedInPredicate));
        } catch (DateTimeParseException ignored) {
            // Ignore parse errors
        }
    }

}