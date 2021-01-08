package com.chqbook.assignment.Assignment.config;

import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoClient {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.uri}")
    private String mongoURI;

    public com.mongodb.client.MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(mongoURI);
        return MongoClients.create(String.valueOf(uri));
    }
}
