package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("grades")
public class Grade extends Model {

    public Long getStudentId()   { return getLong("student_id");   }
    public Long getSubjectId()   { return getLong("subject_id");   }
    public Long getProfessorId() { return getLong("professor_id"); }
    public Double getGrade()     { return getDouble("grade");      }
    public String getDescription(){ return getString("description"); }
    public String getDate()      { return getString("date");        }

    public void setStudentId(Long v)    { set("student_id",   v); }
    public void setSubjectId(Long v)    { set("subject_id",   v); }
    public void setProfessorId(Long v)  { set("professor_id", v); }
    public void setGrade(Double v)      { set("grade",        v); }
    public void setDescription(String v){ set("description",  v); }
    public void setDate(String v)       { set("date",         v); }
}
