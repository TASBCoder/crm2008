package com.crmstudy.domain;

public class Activity {
    private String id;
    private String owner;
    private String name;
    private String start_date;
    private String end_date;
    private String cost;
    private String description;
    private String create_time;
    private String create_by;
    private String edit_time;
    private String edit_by;

    public Activity() {
    }

    public Activity(String id, String owner, String name, String start_date, String end_date, String cost, String description, String create_time, String create_by, String edit_time, String edit_by) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.cost = cost;
        this.description = description;
        this.create_time = create_time;
        this.create_by = create_by;
        this.edit_time = edit_time;
        this.edit_by = edit_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }

    public String getEdit_by() {
        return edit_by;
    }

    public void setEdit_by(String edit_by) {
        this.edit_by = edit_by;
    }

    @Override
    public String toString() {
        return "activityObject{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", cost='" + cost + '\'' +
                ", description='" + description + '\'' +
                ", create_time='" + create_time + '\'' +
                ", create_by='" + create_by + '\'' +
                ", edit_time='" + edit_time + '\'' +
                ", edit_by='" + edit_by + '\'' +
                '}';
    }
}
