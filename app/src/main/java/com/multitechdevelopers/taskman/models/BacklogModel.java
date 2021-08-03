package com.multitechdevelopers.taskman.models;

public class BacklogModel {
String defectName,developerName,status,description;

    public BacklogModel() {

    }

    public BacklogModel(String defectName, String developerName, String status, String description) {
        this.defectName = defectName;
        this.developerName = developerName;
        this.status = status;
        this.description = description;
    }

    public String getDefectName() {
        return defectName;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
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
