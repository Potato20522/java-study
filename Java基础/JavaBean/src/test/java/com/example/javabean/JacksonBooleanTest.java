package com.example.javabean;

import com.example.javabean.bool.User1;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class JacksonBooleanTest {


    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @SneakyThrows
    void user1() {
        User1 user1 = new User1();
        user1.setActive(true);
        user1.setApproved(true);
        user1.setChecked(true);
        user1.setIsNormal(true);
        String json = objectMapper.writeValueAsString(user1);
        System.out.println(json);
        User1 user11 = objectMapper.readValue(json, User1.class);
        System.out.println(user11);

    }
    @Test
    @SneakyThrows
    void user1_2() {
        String json = "{\"active\":true,\"checked\":true,\"isNormal\":true,\"isApproved\":true}";

        User1 user1 = objectMapper.readValue(json, User1.class);
        System.out.println(user1);
    }
}
