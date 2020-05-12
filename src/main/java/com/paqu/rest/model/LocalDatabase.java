package com.paqu.rest.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalDatabase {
    static private LocalDatabase instance = new LocalDatabase();
    static private final Map<Integer, Student> StudentDB = new ConcurrentHashMap<Integer, Student>();
    static private final Map<Integer, Course> CourseDB = new ConcurrentHashMap<Integer, Course>();
    static private AtomicInteger indexCounter;
    static private AtomicInteger courseIdCounter;
    static private AtomicInteger gradeIdCounter;


    static {
        indexCounter  = new AtomicInteger(15000);
        courseIdCounter = new AtomicInteger( 2);
        gradeIdCounter = new AtomicInteger(7);

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



    public int addCourse(Course course)
    {
        int courseId = courseIdCounter.incrementAndGet();
        course.setId(courseId);
        CourseDB.put(courseId, course);
        return courseId;
    }

    public Course removeCourse(int courseId)
    {
        return CourseDB.remove(courseId);
    };

    public Course getCourse(int courseId)
    {
        return CourseDB.get(courseId);
    }

    public Course updateCourse(int courseId, Course update)
    {
        Course current = CourseDB.get(courseId);
        if (current == null) return null;
        current.setName(update.getName());
        current.setLecturer(update.getLecturer());

        CourseDB.put(current.getId(), current);

        return current;
    }
    public Collection<Course> getCourses()
    {
        List<Course> studentList = new ArrayList<Course>(CourseDB.values());
        return studentList;
    }

    public int addGrade(int index, Grade grade)
    {
        Student student = getStudent(index);
        if (student == null) return -1;

        Course course = getCourse(grade.getCourse().getId());
        if (course == null) return -1;

        int gradeId = gradeIdCounter.incrementAndGet();
        grade.setId(gradeId);
        grade.setCourse(course);
        student.getGrades().add(grade);
        updateStudent(index, student);

        return gradeId;
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
