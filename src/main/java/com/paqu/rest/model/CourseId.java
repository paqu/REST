package com.paqu.rest.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity(value = "courseId")
public class CourseId {
    @Id
    private ObjectId id;

    private long lastCourseId;

    public CourseId() {}
    public CourseId (long index)
    {
        this.lastCourseId = index;
    }

    public ObjectId getId()
    {
        return this.id;
    }
    public void setObjectId(ObjectId id)
    {
        this.id = id;
    }
    public long getLastCourseId()
    {
        return this.lastCourseId;
    }
    public void setLastCourseId(long index)
    {
        this.lastCourseId = index;
    }
}

