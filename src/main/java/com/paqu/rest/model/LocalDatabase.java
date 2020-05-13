package com.paqu.rest.model;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalDatabase {

    private final MongoClient client;
    private final Morphia morphia;
    static private Datastore database;

    static private LocalDatabase instance = new LocalDatabase();



    //public Datastore getDatabase() { return database; }
/*
    static {
        initDB(new LocalDatabase());
    }
*/
    private LocalDatabase() {
        this.client = new MongoClient("localhost", 8004);
        this.morphia = new Morphia();
        this.morphia.getMapper().getOptions().setStoreEmpties(true);
        this.morphia.mapPackage("model");
        this.database = this.morphia.createDatastore(client, "SIDBStudents");
        this.database.ensureIndexes();
    }
/*
    public static void initDB(LocalDatabase model) {
        Datastore db = model.getDatabase();

        if (db.getCount(Student.class) > 0 || db.getCount(Course.class) > 0) {
            return;
        }
        Student s1 = new Student(109787, "Pawel", "Kusz", new Date(93, 10, 17), new ArrayList<Grade>());
        Student s2 = new Student(117123, "Piotr", "Kusz", new Date(93, 10, 17), new ArrayList<Grade>());
        Student s3 = new Student(123456, "Romek", "Kaczynski", new Date(65, 9, 23), new ArrayList<Grade>());
        Student s4 = new Student(100000, "Jarek", "Giertych", new Date(45, 10, 1), new ArrayList<Grade>());

    }
  */

    static private final Map<Integer, Student> StudentDB = new ConcurrentHashMap<Integer, Student>();
    static private final Map<Integer, Course> CourseDB = new ConcurrentHashMap<Integer, Course>();
    static private AtomicInteger indexCounter;
    static private AtomicInteger courseIdCounter;
    static private AtomicInteger gradeIdCounter;


    static {
        indexCounter  = new AtomicInteger(109790);
        courseIdCounter = new AtomicInteger( 2);
        gradeIdCounter = new AtomicInteger(7);


        Student s1 = new Student(109787, "Pawel", "Kusz", new Date(93, 10, 17), new ArrayList<Grade>());
        Student s2 = new Student(109788, "Piotr", "Kusz", new Date(93, 10, 17), new ArrayList<Grade>());
        Student s3 = new Student(109789, "Romek", "Kaczynski", new Date(65, 9, 23), new ArrayList<Grade>());
        Student s4 = new Student(109790, "Jarek", "Giertych", new Date(45, 10, 1), new ArrayList<Grade>());


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
        StudentDB.put(109788, s2);
        StudentDB.put(109789, s3);
        StudentDB.put(109790, s4);

        CourseDB.put(1, c1);
        CourseDB.put(2, c2);

        if (database.getCount(Student.class)         == 0
                && database.getCount(Course.class)   == 0
                && database.getCount(Index.class)    == 0
                && database.getCount(GradeId.class)  == 0
                && database.getCount(CourseId.class) == 0)
        {

            database.save(c1);
            database.save(c2);
            CourseId courseId = new CourseId(2);
            database.save(courseId);

            database.save(s1);
            database.save(s2);
            database.save(s3);
            database.save(s4);

            Index index = new Index(109790);
            database.save(index);

            GradeId gradeId = new GradeId(7);
            database.save(gradeId);

            System.out.println("saved");
        }

        System.out.println("Local database initialized");
    }

    public static LocalDatabase getInstance() {
        return instance;
    }

    public long getNextIndex() {
        Index index = database.findAndModify(database.createQuery(Index.class),
                database.createUpdateOperations(Index.class).inc("lastIndex", 1));
        if (index == null) {
            return -1;
        }
        return index.getLastIndex();
    }
/*
    public int addStudent(Student student) {
        int index = indexCounter.incrementAndGet();
        student.setIndex(index);
        StudentDB.put(index, student);
        return index;
    }
    */
    public int addStudent(Student student) {
        int index = (int)getNextIndex();
        student.setIndex(index);
        database.save(student);
        return index;
    }
/*
    public Student getStudent(int index) {
        return StudentDB.get(index);
    }*/

    public Student getStudent(long index) {
        return database.createQuery(Student.class).filter("index", index).first();
    }

/*
    public Collection<Student> getStudents() {
        List<Student> studentList = new ArrayList<Student>(StudentDB.values());
        return studentList;
    }
    */

    public Collection<Student> getStudents() {
        List<Student> studentList = database.createQuery(Student.class).find().toList();
        return studentList;
    }

/*
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
*/
    public Student updateStudent(int index, Student update)
    {
        Student current = getStudent(index);
        if (current == null) return null;

        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setBirthday(update.getBirthday());

        database.save(current);

        return current;

    }
    /*
    public Student removeStudent(int index)
    {
        return StudentDB.remove(index);
    };

     */

    public Student removeStudent(int index)
    {
        Student student = getStudent(index);
        if (student == null)
            return null;

        database.delete(student);

        return student;
    };




    public long getNextCourseId() {
        CourseId courseId = database.findAndModify(database.createQuery(CourseId.class),
                database.createUpdateOperations(CourseId.class).inc("lastCourseId", 1));
        if (courseId == null) {
            return -1;
        }
        return courseId.getLastCourseId();
    }
/*
    public int addCourse(Course course)
    {
        int courseId = courseIdCounter.incrementAndGet();
        course.setId(courseId);
        CourseDB.put(courseId, course);
        return courseId;
    }
*/
    public int addCourse(Course course)
    {
        int courseId = (int)getNextCourseId();
        course.setId(courseId);
        database.save(course);
        return courseId;
    }

    public Course removeCourse(int courseId)
    {
        return CourseDB.remove(courseId);
    };
/*
    public Course getCourse(int courseId)
    {
        return CourseDB.get(courseId);
    }
 */
    public Course getCourse(int courseId)
    {
        return database.createQuery(Course.class).filter("id", courseId).first();
    }
    /*
    public Collection<Course> getCourses()
    {
        List<Course> studentList = new ArrayList<Course>(CourseDB.values());
        return studentList;
    }
     */
    public Collection<Course> getCourses()
    {
        List<Course> courseList = database.createQuery(Course.class).find().toList();
        return courseList;
    }
/*
    public Course updateCourse(int courseId, Course update)
    {
        Course current = CourseDB.get(courseId);
        if (current == null) return null;
        current.setName(update.getName());
        current.setLecturer(update.getLecturer());

        CourseDB.put(current.getId(), current);

        return current;
    }
*/
    public Course updateCourse(int courseId, Course update)
    {
        Course current = getCourse(courseId);
        if (current == null) return null;
        current.setName(update.getName());
        current.setLecturer(update.getLecturer());

        database.save(current);
        return current;
    }

    public long getNextGradeId() {
        GradeId gradeId = database.findAndModify(database.createQuery(GradeId.class),
                database.createUpdateOperations(GradeId.class).inc("lastGradeId", 1));
        if (gradeId == null) {
            return -1;
        }
        return gradeId.getLastGradeId();
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
