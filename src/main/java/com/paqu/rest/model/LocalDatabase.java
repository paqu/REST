package com.paqu.rest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalDatabase {
    static private LocalDatabase instance = new LocalDatabase();
    static private final Map<Integer, Student> StudentDB = new ConcurrentHashMap<Integer, Student>();
    static private final Map<Integer, Course> CourseDB = new ConcurrentHashMap<Integer, Course>();

    static {
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
        return instance; }

    public Map<Integer, Student> getStudents() {

        return this.StudentDB;
    }
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
