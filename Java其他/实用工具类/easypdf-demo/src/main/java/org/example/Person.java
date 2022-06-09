package org.example;

import lombok.Data;
import org.example.table.TableProperty;

@Data
public class Person {
    @TableProperty("id")
    private Long id;
    private Integer age;
    private Gender gender;
    private Float weight;
    private Float height;
    private String address;

    public enum Gender {
        Man,Woman
    }

}
