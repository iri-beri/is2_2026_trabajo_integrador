package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("plans")
public class StudyPlan extends Model{
     public String getVersion() {
        return getString("version");
    }

    public void setVersion(String version) {
        set("version", version);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }
}
