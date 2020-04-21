package com.paqu.rest.resource;

import com.paqu.rest.model.LocalDatabase;
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


@Path("/updates")
public class CourseResource {

    // local database for students
    static private final Map<Integer, Course> CourseDB = LocalDatabase.getInstance().getCourses();

    static private AtomicInteger idCounter = new AtomicInteger(2);

    @POST
    @Consumes("application/xml")
    public Response createCourse(Course update) {
        if ((update.getName() != null && update.getName().equals(""))
                || (update.getLecturer() != null && update.getLecturer().equals(""))
                || update.getName()  == null || update.getLecturer() == null
        ) {
            return Response.status(400).build();
        }
        update.setId(idCounter.incrementAndGet());
        CourseDB.put(update.getId(), update);
        return Response.created(URI.create("/updates/" + update.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Course getCourse(@PathParam("id") int id) {
        Course update = CourseDB.get(id);
        if (update == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return update;
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public Response updateCourse(@PathParam("id") int id, Course update)  {
        if ((update.getName() != null && update.getName().equals(""))
                || (update.getLecturer() != null && update.getLecturer().equals(""))
                || update.getName()  == null || update.getLecturer() == null
        ) {
            return Response.status(400).build();
        }

        Course current = CourseDB.get(id);
        if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        current.setName(update.getName());
        current.setLecturer(update.getLecturer());
        CourseDB.put(current.getId(), current);
        return Response.status(204).build();
    }

    @DELETE
    @Path("{id}")
    public void deleteCourse(@PathParam("id") int id) {
        Course current = CourseDB.remove(id);
        if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Produces({ "application/xml"})
    public Collection<Course> getCourses() {
        List<Course> updateList = new ArrayList<Course>(CourseDB.values());
        return updateList;
    }
}