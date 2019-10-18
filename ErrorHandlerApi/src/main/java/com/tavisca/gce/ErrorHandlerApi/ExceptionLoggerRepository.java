package com.tavisca.gce.ErrorHandlerApi;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionLoggerRepository extends MongoRepository<ExceptionResponse, Integer> {
}
