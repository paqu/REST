package com.paqu.rest.model;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalDatabase {
    static private LocalDatabase instance = new LocalDatabase();
    static private final Map<Integer, Student> StudentDB = new ConcurrentHashMap<Integer, Student>();
    static private final Map<Integer, Course> CourseDB = new ConcurrentHashMap<Integer, Course>();
    static private AtomicInteger indexCounter;

    static {
        indexCounter  = new AtomicInteger(15000);
        Student s1 = new Student(109787, "Pawel", "Kusz", new Date(93, 10, 17), new ArrayList<Grade>());
        Student s2 = new Student(117123, "Piotr", "Kusz", new Date(93, 10, 17), new ArrayList<Grade>());
        Student s3 = new Student(123456, "Romek", "Kaczynski", new Date(65, 9, 23), new ArrayList<Grade>());
        Student s4 = new Student(100000, "Jarek", "Giertych", new Date(45, 10, 1), new ArrayList<Grade>());

        Course c1 = new Course (1,"Technologie Programistyczne - Systemy internetowe", "Tomasz Pawlak");
        Course c2 = new Course (2,"Biznesowe Systemy Rozproszone", "Tomasz Pawlak");


        s1.getGrades().add(new Grade(1, 2.0f, new Date(), c1));
        s1.getGrades().add(new Grade(2, 3.5f, new Date(), c1));
        s1.getGrades().add(new Grade(3, 3.5f, new Date(), c2));

        s2.getGrades().add(new Grade(4, 5.0f, new Date(), c2));
        s2.getGrades().add(new Grade(5, 5.0f, new Date(), c1));

        s3.getGrades().add(new Grade(6, 2.0f, new Date(), c2));
        s3.getGrades().add(new Grade(7, 2.0f, new Date(), c2));

        StudentDB.put(109787, s1);
        StudentDB.put(117123, s2);
        StudentDB.put(123456, s3);
        StudentDB.put(100000, s4);

        CourseDB.put(1, c1);
        CourseDB.put(2, c2);

        System.out.println("Local database initialized");
    }

    public static LocalDatabase getInstance() {
        return instance;
    }

    public int addStudent(Student student) {
        int index = indexCounter.incrementAndGet();
        student.setIndex(index);
        StudentDB.put(index, student);
        return index;
    }
    public Student getStudent(int index) {
        return StudentDB.get(index);
    }
    public Collection<Student> getStudents() {
        List<Student> studentList = new ArrayList<Student>(StudentDB.values());
        return studentList;
    }
    public Student updateStudent(int index, Student update)
    {
        Student current = StudentDB.get(index);
        if (current == null) return null;

        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setBirthday(update.getBirthday());

        StudentDB.put(current.getIndex(), current);

        return current;

    }

    public Student removeStudent(int index)
    {
        return StudentDB.remove(index);
    };




    public Map<Integer, Course> getCourses() {
        return this.CourseDB;
    }

    synchronized public void removeGradesWithCourseId(int courseID) {
        for (var student : StudentDB.values()) {
            for (Iterator<Grade> it = student.getGrades().iterator(); it.hasNext();) {
                Grade grade = it.next();
                if (grade.getCourse().getId() == courseID)
                    it.remove();
            }
        }
    }
}
