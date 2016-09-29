package com.attmm.admin.projectt.view;

/**
 * Created by imo on 2016/9/28.
 */
public class History {
    private String taskTitle,taskContent,taskStatus,taskDate;

    public History(){}
    public History(String taskTitle,String taskContent,String taskStatus,String taskDate){
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.taskDate = taskDate;
    }
    public String getTaskTitle(){
        return taskTitle;
    }
    public String getTaskContent(){
        return taskContent;
    }
    public String getTaskStatus(){
        return taskStatus;
    }
    public String getTaskDate(){
        return taskDate;
    }

}
