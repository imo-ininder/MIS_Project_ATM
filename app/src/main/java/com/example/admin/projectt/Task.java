package com.example.admin.projectt;

/**
 * Created by imo on 2016/6/29.
 */
public class Task {
    private String taskTittle;
    private String taskContent;
    private String taskLocation;
    private Double longitude ,latitude;
    public Task(){}
    public Task(String taskTittle,String taskLocation,String taskContent,Double longitude,Double latitude) {
        this.taskTittle = taskTittle;
        this.taskContent = taskContent;
        this.taskLocation = taskLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public String getTaskTittle(){
        return taskTittle;
    }
    public String getTaskLocation(){
        return taskLocation;
    }
    public String getTaskContent(){
        return taskContent;
    }
}
