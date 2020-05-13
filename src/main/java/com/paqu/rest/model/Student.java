package com.paqu.rest.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.paqu.rest.utils.ObjectIdJaxbAdapter;
import dev.morphia.annotations.*;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement(name="student")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity(value = "students")
public class Student {


    @Id
    @XmlTransient
    private ObjectId _id;

    @Indexed(options = @IndexOptions(unique = true))
    private int index;
    private String firstName;
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Warsaw")
    private Date birthday;

    @Embedded
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

    public ObjectId getId()
    {
        return this._id;
    }

    public void setId(ObjectId id)
    {
        this._id = id;
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

    @InjectLinks({
            @InjectLink(value = "/students/{index}", rel = "self"),
            @InjectLink(value = "/students/", rel = "parent"),
            @InjectLink(value = "/students/{index}/grades", rel = "grades")
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    @Transient
    List<Link> links;
}

