package com.chqbook.assignment.Assignment.common;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdGenerator {
    public static String getId() {
        return UUID.randomUUID().toString();
    }
}
