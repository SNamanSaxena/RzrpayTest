package com.chqbook.assignment.Assignment.queries;

import com.chqbook.assignment.Assignment.model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends MongoRepository<Orders, String> {

    @Query("{'order_Id' : ?0}")
    Orders findByOrderId(String id);
}
