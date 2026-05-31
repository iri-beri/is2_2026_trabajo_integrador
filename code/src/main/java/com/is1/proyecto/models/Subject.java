package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("subjects")
public class Subject extends Model {

    public Integer getCode() {
        return getInteger("code");
    }

    public void setCode(Integer code) {
        set("code", code);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getCourseSyllabus() {
        return getString("course_syllabus");
    }

    public void setCourseSyllabus(String courseSyllabus) {
        set("course_syllabus", courseSyllabus);
    }

    public Integer getHours() {
        return getInteger("hours");
    }

    public void setHours(Integer hours) {
        set("hours", hours);
    }
}