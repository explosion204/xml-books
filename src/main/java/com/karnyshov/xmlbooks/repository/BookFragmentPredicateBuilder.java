package com.karnyshov.xmlbooks.repository;

import com.karnyshov.xmlbooks.model.QBookFragment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public class BookFragmentPredicateBuilder {
    private final BooleanBuilder booleanBuilder = new BooleanBuilder();

    public BookFragmentPredicateBuilder type(String type) {
        if (type != null) {
            QBookFragment qBookFragment = QBookFragment.bookFragment;
            BooleanExpression typePredicate = qBookFragment.type.containsIgnoreCase(type);
            booleanBuilder.and(typePredicate);
        }

        return this;
    }

    public BookFragmentPredicateBuilder title(String title) {
        if (title != null) {
            QBookFragment qBookFragment = QBookFragment.bookFragment;
            BooleanExpression typePredicate = qBookFragment.title.containsIgnoreCase(title);
            booleanBuilder.and(typePredicate);
        }

        return this;
    }

    public Predicate build() {
        return booleanBuilder;
    }
}
