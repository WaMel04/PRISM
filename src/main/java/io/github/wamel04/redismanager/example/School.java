package io.github.wamel04.redismanager.example;

import io.github.wamel04.redismanager.rediobject.RediObject;

import java.util.List;

public class School implements RediObject {

    List<Student> students;
    Country country;
    String principilName = "엄준식";

    @Override
    public String getNameKey() {
        return "school";
    }

    public School(List<Student> students, Country country, String principilName) {
        this.students = students;
        this.country = country;
        this.principilName = principilName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Country getCountry() {
        return country;
    }

    public String getPrincipilName() {
        return principilName;
    }

}
