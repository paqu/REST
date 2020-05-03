package com.paqu.rest.model;

import com.paqu.rest.resource.CourseResource;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

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

    @InjectLinks({
            @InjectLink(resource = CourseResource.class, method="getCourse", rel = "self"),
            @InjectLink(resource = CourseResource.class, rel = "parent"),

    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

}
