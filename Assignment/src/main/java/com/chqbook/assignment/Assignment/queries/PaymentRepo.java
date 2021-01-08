package com.chqbook.assignment.Assignment.queries;

import com.chqbook.assignment.Assignment.model.Payments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepo extends MongoRepository<Payments, String> {
}
