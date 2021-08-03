package com.multitechdevelopers.taskman.models;

public class ProjectListModel {
    private String mProjectId;
    private String mProjectName;

    public ProjectListModel() {}  // Needed for Firebase

    public ProjectListModel(String projectId, String projectName) {
        mProjectId = projectId;
        mProjectName = projectName;
    }

    public String getProjectId() { return mProjectId; }

    public void setProjectId(String projectId) { mProjectId = projectId; }

    public String getProjectName() { return mProjectName; }

    public void setProjectName(String projectName) { mProjectName = projectName; }
}
