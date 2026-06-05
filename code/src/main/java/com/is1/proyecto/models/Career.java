package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;
import org.javalite.activejdbc.annotations.Table;

@Table("careers")
@Many2Many(other = Subject.class, join = "career_subjects", sourceFKName = "career_id", targetFKName = "subject_id")
public class Career extends Model {

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
}
