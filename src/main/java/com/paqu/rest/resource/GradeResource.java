package com.paqu.rest.resource;

import com.paqu.rest.model.LocalDatabase;
import com.paqu.rest.model.Student;
import com.paqu.rest.model.Grade;
import com.paqu.rest.model.Course;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.ws.rs.*;
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
    public Response getGrades(@PathParam("index") int index,
                              @DefaultValue("0") @QueryParam("course") String courseId_,
                              @DefaultValue("-1") @QueryParam("value") float value,
                              @QueryParam("valueCompare") int valueCompare,
                              @QueryParam("date") String date,
                              @QueryParam("dateCompare") int dateCompare) throws ParseException {
        Student student = LocalDatabase.getInstance().getStudent(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

        int courseId = 0;
        try {
            courseId = Integer.parseInt(courseId_);
        } catch (Exception e) {
            return Response.ok(new ArrayList<Grade>()).build();

        }
        List<Grade> gradesList = new ArrayList<Grade>(student.getGrades());

        if (courseId != 0) {
            int finalCourseId = courseId;
            gradesList = gradesList.stream()
                    .filter(grade -> grade.getCourse().getId() == finalCourseId)
                    .collect(Collectors.toList());
        }
        if (value >= 0) {
            if (valueCompare == 1) {
                gradesList = gradesList.stream()
                        .filter(grade -> grade.getValue() >= value)
                        .collect(Collectors.toList());
            } else if (valueCompare == 0) {
                gradesList = gradesList.stream()
                        .filter(grade -> grade.getValue() == value)
                        .collect(Collectors.toList());
            } else if (valueCompare == -1) {
                gradesList = gradesList.stream()
                        .filter(grade -> grade.getValue() <= value)
                        .collect(Collectors.toList());
            } else
                return Response.status(400).build();
        }

        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date gradeDate = formatter.parse(date);

            if (dateCompare == 1){
                gradesList = gradesList.stream()
                        .filter(grade -> grade.getDate().after(gradeDate))
                        .collect(Collectors.toList());
            }
            else if (dateCompare == 0){
                gradesList = gradesList.stream()
                        .filter(grade -> grade.getDate().equals(gradeDate))
                        .collect(Collectors.toList());
            }
            else if (dateCompare == -1){
                gradesList = gradesList.stream()
                        .filter(grade -> grade.getDate().before(gradeDate))
                        .collect(Collectors.toList());
            }
            else
                return Response.status(400).build();
        }

        return Response.ok(gradesList).build();
    }
}