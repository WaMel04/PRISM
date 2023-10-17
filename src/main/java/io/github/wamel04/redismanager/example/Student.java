package io.github.wamel04.redismanager.example;

import io.github.wamel04.redismanager.rediobject.RediObject;

import java.util.HashMap;

public class Student implements RediObject {
    private String name;
    private String sex;
    private int age;
    private HashMap<String, String> trashMap;

    public Student(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    @Override
    public String getNameKey() {
        return "student";
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

}
