package com.paqu.rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name="grade")
public class Grade {
    private int id;
    private double value;
    private Date date;
    private Course course;

    public Grade () {};
    public Grade (int id, double value, Date date, Course course)
    {
        this.id = id;
        this.value = value;
        this.date = date;
        this.course = course;
    }
    public int getId() { return this.id; }
    public void setId(int id) { this.id = id;}

    public double getValue() { return this.value; }
    public void setValue(double value) { this.value = value; }

    public Course getCourse() { return this.course; }
    public void setCourse(Course course) { this.course = course; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", value=" + value +
                ", date=" + date +
                ", course=" + course +
                '}';
    }
}
