package com.karnyshov.xmlbooks.repository;

import com.karnyshov.xmlbooks.model.BookFragment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BookFragmentRepository extends MongoRepository<BookFragment, String>,
        QuerydslPredicateExecutor<BookFragment> {
    List<BookFragment> findByNextFragmentId(String nextFragmentId);
}
