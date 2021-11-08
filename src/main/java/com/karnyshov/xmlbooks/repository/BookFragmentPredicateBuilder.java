package com.karnyshov.xmlbooks.repository;

import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.model.QBookFragment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;


/**
 * This class is used to build a predicate for filtering {@link BookFragment} entites.
 */
public class BookFragmentPredicateBuilder {
    private final BooleanBuilder booleanBuilder = new BooleanBuilder();

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
