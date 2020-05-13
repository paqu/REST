package com.paqu.rest.resource;

import com.paqu.rest.model.LocalDatabase;
import com.paqu.rest.model.Student;
import com.paqu.rest.model.Grade;
import com.paqu.rest.model.Course;

import java.net.URI;
import java.util.*;
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


    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createGrade(@PathParam("index") int index, Grade grade) {
        if (grade.getDate() == null
                || grade.getCourse() == null
                || grade.getValue() < 2.0
                || grade.getValue() > 5.0
                || grade.getValue() % 0.5 != 0
        ) {
            return Response.status(400).build();
        }

        int gradeId = LocalDatabase.getInstance().addGrade(index, grade);
        if (gradeId == -1) {
            throw new WebApplicationException(400);
        }
        return Response.created(URI.create("/students/" + index + "/grades/" + gradeId)).build();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Grade getGrade(@PathParam("index") int index, @PathParam("id") int id) {
        Grade grade = null;
        Student student = LocalDatabase.getInstance().getStudent(index);
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
    @Consumes({"application/xml", "application/json"})
    public Response updateGrade(@PathParam("index") int index, @PathParam("id") int id, Grade update)  {

        Student student = LocalDatabase.getInstance().getStudent(index);
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

        Course course = LocalDatabase.getInstance().getCourse(update.getCourse().getId());
        if (course == null) throw new WebApplicationException(400); //to meet test expect, should be 404 in my opinion

        grade.setValue(update.getValue());
        grade.setDate(update.getDate());
        grade.setCourse(course);
        LocalDatabase.getInstance().updateStudentGrades(index, student.getGrades());
        return Response.status(204).build();
    }

    @DELETE
    @Path("{id}")
    public void deleteGrade(@PathParam("index") int index, @PathParam("id") int id) {
        Grade grade = null;
        Student student = LocalDatabase.getInstance().getStudent(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        for (var g : student.getGrades()){
            if (g.getId() == id) {
                grade = g;
            }
        }

        if (grade == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        student.getGrades().remove(grade);
        LocalDatabase.getInstance().updateStudentGrades(index, student.getGrades());
    }

    @GET
    @Produces({ "application/xml", "application/json"})
    public Collection<Grade> getStudents(@PathParam("index") int index) {
        Student student = LocalDatabase.getInstance().getStudent(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        List<Grade> gradeList = new ArrayList<Grade>(student.getGrades());
        return gradeList;
    }
}