package com.paqu.rest.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity(value = "studentIndex")
public class Index {
    @Id
    private ObjectId id;

    private long lastIndex;

    public Index() {}
    public Index (long index)
    {
        this.lastIndex = index;
    }

    public ObjectId getId()
    {
        return this.id;
    }
    public void setObjectId(ObjectId id)
    {
        this.id = id;
    }
    public long getLastIndex()
    {
        return this.lastIndex;
    }
    public void setLastIndex(long index)
    {
        this.lastIndex = index;
    }
}
