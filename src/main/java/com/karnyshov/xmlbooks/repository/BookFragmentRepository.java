package com.karnyshov.xmlbooks.repository;

import com.karnyshov.xmlbooks.model.BookFragment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookFragmentRepository extends MongoRepository<BookFragment, Long> {

}
