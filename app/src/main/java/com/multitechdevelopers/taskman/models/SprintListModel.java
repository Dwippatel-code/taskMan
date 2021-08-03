package com.multitechdevelopers.taskman.models;

public class SprintListModel {
private String taskName,assignedPerson, deadline,description,sprintId;

    public SprintListModel() {

    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public SprintListModel(String sprintId) {
        this.sprintId = sprintId;
    }

    public SprintListModel(String taskName, String assignedPerson, String deadline, String description) {
        this.taskName = taskName;
        this.assignedPerson = assignedPerson;
        this.deadline = deadline;
        this.description = description;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(String assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSprintId() {
        return sprintId;
    }
}
