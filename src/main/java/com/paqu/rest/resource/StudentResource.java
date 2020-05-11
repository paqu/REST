package com.paqu.rest.resource;

import com.paqu.rest.model.LocalDatabase;
import com.paqu.rest.model.Student;

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


@Path("/students")
public class StudentResource {

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createStudent(Student student) {
        int index;
        if ((student.getLastName() != null && student.getLastName().equals(""))
                || (student.getFirstName() != null && student.getFirstName().equals(""))
                || student.getBirthday()  == null || student.getFirstName() == null || student.getLastName() == null
        ) {
            return Response.status(400).build();
        }
        index = LocalDatabase.getInstance().addStudent(student);
        return Response.created(URI.create("/students/" + index)).build();
    }

    @GET
    @Path("{index}")
    @Produces({"application/xml", "application/json"})
    public Student getStudent(@PathParam("index") int index) {
        Student student = LocalDatabase.getInstance().getStudent(index);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return student;
    }

    @PUT
    @Path("{index}")
    @Consumes({"application/xml", "application/json"})
    public Response updateStudent(@PathParam("index") int index, Student update)  {
        if ((update.getLastName() != null && update.getLastName().equals(""))
                || (update.getFirstName() != null && update.getFirstName().equals(""))
                || update.getBirthday()  == null || update.getFirstName() == null || update.getLastName() == null
        ) {
            return Response.status(400).build();
        }

        if (LocalDatabase.getInstance().updateStudent(index, update) == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        return Response.status(204).build();
    }

    @DELETE
    @Path("{index}")
    public void deleteStudent(@PathParam("index") int index) {
        Student current = LocalDatabase.getInstance().removeStudent(index);
        if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public Collection<Student> getStudents() {
        return LocalDatabase.getInstance().getStudents();
    }
}