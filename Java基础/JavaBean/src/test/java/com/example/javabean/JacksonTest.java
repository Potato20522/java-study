package com.example.javabean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;


public class JacksonTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @SneakyThrows
    void normal() {
        Person person = new Person();
        person.setId(1L);
        person.setName("zhangsan");
        String json = objectMapper.writeValueAsString(person);
        System.out.println(json);
        Person person2 = objectMapper.readValue(json, Person.class);
        System.out.println(person2);
    }

    @SneakyThrows
    @Test
    void noGetSet() {
        PersonNoGetSet person = new PersonNoGetSet();
        person.id=1L;
        person.name = "zhangsan";
        String json = objectMapper.writeValueAsString(person);
        System.out.println(json);
        PersonNoGetSet person2 = objectMapper.readValue(json, PersonNoGetSet.class);
        System.out.println(person2);
    }
}
