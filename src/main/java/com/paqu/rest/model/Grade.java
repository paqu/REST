package com.paqu.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paqu.rest.resource.GradeResource;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement(name="grade")
public class Grade {
    private int id;
    private double value;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Warsaw")
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
    @InjectLinks({
            @InjectLink(resource = GradeResource.class, method="getGrade", rel = "self"),
            @InjectLink(resource = GradeResource.class, rel = "parent"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;
}
