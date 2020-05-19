package com.paqu.rest.resource;

import com.paqu.rest.model.LocalDatabase;
import com.paqu.rest.model.Course;
import dev.morphia.query.Query;

import java.net.URI;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;


@Path("/courses")
public class CourseResource {

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createCourse(Course course) {
        int courseId;
        if ((course.getName() != null && course.getName().equals(""))
                || (course.getLecturer() != null && course.getLecturer().equals(""))
                || course.getName()  == null || course.getLecturer() == null
        ) {
            return Response.status(400).build();
        }
        courseId = LocalDatabase.getInstance().addCourse(course);

        return Response.created(URI.create("/courses/" + courseId)).build();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Course getCourse(@PathParam("id") int id) {
        Course course = LocalDatabase.getInstance().getCourse(id);
        if (course == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return course;
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public Response updateCourse(@PathParam("id") int id, Course update)  {

        if ((update.getName() != null && update.getName().equals(""))
                || (update.getLecturer() != null && update.getLecturer().equals(""))
                || update.getName()  == null || update.getLecturer() == null
        ) {
            return Response.status(400).build();
        }

        if (LocalDatabase.getInstance().updateCourse(id, update) == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        return Response.status(204).build();
    }
    @DELETE
    @Path("{id}")
    public void deleteCourse(@PathParam("id") int id) {
        LocalDatabase.getInstance().removeGradesWithCourseId(id);
        Course current = LocalDatabase.getInstance().removeCourse(id);
        if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Produces({ "application/xml", "application/json"})
    public Collection<Course> getCourses(

            @QueryParam("name") String name,
            @QueryParam("lecturer") String lecturer
    ) {

        return LocalDatabase.getInstance().getCourses(name, lecturer);
    }
}