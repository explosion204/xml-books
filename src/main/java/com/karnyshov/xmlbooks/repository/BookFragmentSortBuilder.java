package com.karnyshov.xmlbooks.repository;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookFragmentSortBuilder {
    private final List<Sort.Order> orders = new ArrayList<>();

    public enum SortField {
        SECTION, TITLE;

        public static boolean hasField(String fieldName) {
            return Arrays.stream(values())
                    .map(Enum::name)
                    .anyMatch(value -> value.equalsIgnoreCase(fieldName));
        }
    }

    public BookFragmentSortBuilder byField(Sort.Direction direction, String fieldName) {
        if (SortField.hasField(fieldName)) {
            Sort.Order order = new Sort.Order(direction, fieldName);
            orders.add(order);
        }

        return this;
    }

    public Sort build() {
        return Sort.by(orders);
    }
}
