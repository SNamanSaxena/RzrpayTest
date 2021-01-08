package com.chqbook.assignment.Assignment.config;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {
    private static MappingMongoConverter mappingConverter;
    @Value("${spring.data.mongodb.database}")
    private String database;

    @Autowired
    private MongoClient mongoclient;

    @Primary
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoClientDbFactory(mongoclient.mongoClient(), database);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoDbFactory factory = mongoDbFactory();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);

        // Remove _class
        try {
            mappingConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
            mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        } catch (NoSuchBeanDefinitionException ignore) {
        }
        return new MongoTemplate(factory, mappingConverter);

    }
}
