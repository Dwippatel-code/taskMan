package com.multitechdevelopers.taskman.models;

public class UserStoryModel {
    String userName, acceptanceCriteria,developer,priority,status,description;

    public UserStoryModel() {

    }

    public UserStoryModel(String userName, String acceptanceCriteria, String developer, String priority, String status, String description) {
        this.userName = userName;
        this.acceptanceCriteria = acceptanceCriteria;
        this.developer = developer;
        this.priority = priority;
        this.status = status;
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
