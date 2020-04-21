package com.paqu.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="course")
public class Course {
    private int id;
    private String name;
    private String lecturer;

    public Course () {};
    public Course (int id, String name, String lecturer)
    {
        this.id = id;
        this.name = name;
        this.lecturer = lecturer;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return this.lecturer;
    }
    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lecturer='" + lecturer + '\'' +
                '}';
    }
}
