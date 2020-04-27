package com.paqu.rest.resource;

import com.paqu.rest.model.LocalDatabase;
import com.paqu.rest.model.Student;
import com.paqu.rest.model.Grade;
import com.paqu.rest.model.Course;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


@Path("/students/{index}/grades")
public class GradeResource {

    // local database for students
    static private final Map<Integer, Student> StudentDB = LocalDatabase.getInstance().getStudents();
    static private final Map<Integer, Course>  CourseDB  = LocalDatabase.getInstance().getCourses();

    static private AtomicInteger idCounter = new AtomicInteger(7);

    @POST
    @Consumes("application/xml")
    public Response createGrade(@PathParam("index") int index, Grade grade) {
        Student student = StudentDB.get(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        if (grade.getDate() == null
                || grade.getCourse() == null
                || grade.getValue() < 2.0
                || grade.getValue() > 5.0
                || grade.getValue() % 0.5 != 0
        ) {
            return Response.status(400).build();
        }

        Course course   = CourseDB.get(grade.getCourse().getId());
        if (course == null) throw new WebApplicationException(400); //to meet test expect, should be 404 in my opinion

        grade.setId(idCounter.incrementAndGet());
        grade.setCourse(course);
        student.getGrades().add(grade);
        StudentDB.put(index, student);
        return Response.created(URI.create("/students/" + student.getIndex() + "/grades/" + grade.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Grade getGrade(@PathParam("index") int index, @PathParam("id") int id) {
        Grade grade = null;
        Student student = StudentDB.get(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        for (var g : student.getGrades()){
            if (g.getId() == id) {
                grade = g;
            }
        }
        if (grade == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        return grade;
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public Response updateGrade(@PathParam("index") int index, @PathParam("id") int id, Grade update)  {

        Student student = StudentDB.get(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        Grade grade = null;
        for (var g : student.getGrades()){
            if (g.getId() == id) {
                grade = g;
            }
        }
        if (grade == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        if (update.getDate() == null
                || update.getCourse() == null
                || update.getValue() < 2.0
                || update.getValue() > 5.0
                || update.getValue() % 0.5 != 0
        ) {
            return Response.status(400).build();
        }

        Course course   = CourseDB.get(update.getCourse().getId());
        if (course == null) throw new WebApplicationException(400); //to meet test expect, should be 404 in my opinion

        grade.setValue(update.getValue());
        grade.setDate(update.getDate());
        grade.setCourse(course);

        StudentDB.put(index, student);
        return Response.status(204).build();
    }

    @DELETE
    @Path("{id}")
    public void deleteGrade(@PathParam("index") int index, @PathParam("id") int id) {
        Grade grade = null;
        Student student = StudentDB.get(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        for (var g : student.getGrades()){
            if (g.getId() == id) {
                grade = g;
            }
        }

        if (grade == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        student.getGrades().remove(grade);
        StudentDB.put(index, student);
    }

    @GET
    @Produces({ "application/xml"})
    public Collection<Grade> getStudents(@PathParam("index") int index) {
        Student student = StudentDB.get(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        List<Grade> gradeList = new ArrayList<Grade>(student.getGrades());
        return gradeList;
    }
}