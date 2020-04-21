package com.paqu.rest.model;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;

@XmlRootElement(name="student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student {

    private int index;
    private String firstName;
    private String lastName;
    private Date birthday;
    @XmlTransient
    private ArrayList<Grade> grades;

    public Student() {}

    public Student(int index, String firstName, String lastName, Date birthday, ArrayList<Grade> grades)
    {
        this.index = index;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.grades = grades;

    }

    public int getIndex() {
        return this.index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthday(Date birthday) { this.birthday = birthday; }
    public Date getBirthday() { return this.birthday; }

    public ArrayList<Grade> getGrades() {
        return this.grades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Student{" +
                "index=" + index +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", grades=" + grades +
                '}';
    }
}

