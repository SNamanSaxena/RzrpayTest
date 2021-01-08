package com.chqbook.assignment.Assignment.common;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class DateOps {
    public static Date GetCurrentTimeUTC() {
        return Date.from(LocalDateTime.now(ZoneId.of("UTC")).toInstant(ZoneOffset.UTC));
    }
}
