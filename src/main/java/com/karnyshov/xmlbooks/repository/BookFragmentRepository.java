package com.karnyshov.xmlbooks.repository;

import com.karnyshov.xmlbooks.model.BookFragment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

/**
 * Implementors of the interface provide functionality for accessing {@link BookFragment} entities.
 */
public interface BookFragmentRepository extends MongoRepository<BookFragment, String>,
        QuerydslPredicateExecutor<BookFragment> {

    /**
     * Find book fragment by unique id of the linked fragment.
     *
     * @param nextFragmentId the next fragment id
     * @return list of {@link BookFragment} objects
     */
    List<BookFragment> findByNextFragmentId(String nextFragmentId);
}
