package com.paqu.rest.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity(value = "gradeId")
public class GradeId {
    @Id
    private ObjectId id;
    private long lastGradeId;

    public GradeId() {}
    public GradeId (long index)
    {
        this.lastGradeId = index;
    }

    public ObjectId getId()
    {
        return this.id;
    }
    public void setObjectId(ObjectId id)
    {
        this.id = id;
    }
    public long getLastGradeId()
    {
        return this.lastGradeId;
    }
    public void setLastGradeId(long index)
    {
        this.lastGradeId = index;
    }
}

