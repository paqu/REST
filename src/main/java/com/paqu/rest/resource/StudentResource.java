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

    // local database for students
    static private final Map<Integer, Student> StudentDB = LocalDatabase.getInstance().getStudents();

    static private AtomicInteger idCounter = new AtomicInteger(117787);

    @POST
    @Consumes("application/xml")
    public Response createStudent(Student student) {
        if ((student.getLastName() != null && student.getLastName().equals(""))
                || (student.getFirstName() != null && student.getFirstName().equals(""))
                || student.getBirthday()  == null || student.getFirstName() == null || student.getLastName() == null
        ) {
            return Response.status(400).build();
        }
        student.setIndex(idCounter.incrementAndGet());
        StudentDB.put(student.getIndex(), student);
        return Response.created(URI.create("/students/" + student.getIndex())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Student getStudent(@PathParam("id") int id) {
        Student student = StudentDB.get(id);
        if (student == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return student;
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public Response updateStudent(@PathParam("id") int id, Student update)  {
        if ((update.getLastName() != null && update.getLastName().equals(""))
                || (update.getFirstName() != null && update.getFirstName().equals(""))
                || update.getBirthday()  == null || update.getFirstName() == null || update.getLastName() == null
        ) {
            return Response.status(400).build();
        }

        Student current = StudentDB.get(id);
        if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setBirthday(update.getBirthday());
        StudentDB.put(current.getIndex(), current);
        return Response.status(204).build();
    }

    @DELETE
    @Path("{id}")
    public void deleteStudent(@PathParam("id") int id) {
        Student current = StudentDB.remove(id);
        if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Produces({ "application/xml"})
    public Collection<Student> getStudents() {
        List<Student> studentList = new ArrayList<Student>(StudentDB.values());
        return studentList;
    }
}