package com.example.attendancesystem;


import com.google.firebase.Timestamp;


public class YourModelClass {
    private String username;
    private String usn;
    private String semester;
    private Timestamp timestamp;
    private String attendanceStatus;


    public YourModelClass() {
        // Default constructor (required for Firebase)
    }

    public YourModelClass(String username, String usn, String semester) {
        this.username = username;
        this.usn = usn;
        this.semester = semester;
        this.timestamp = timestamp;
        this.attendanceStatus = attendanceStatus;

    }

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Timestamp gettimestamp() {
        return timestamp;
    }

    public void settimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getattendanceStatus() {  return attendanceStatus;    }

    public void setattendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getusn() {
        return usn;
    }

    public void setusn(String usn) {
        this.usn = usn;
    }

    public String getSemester() {  return semester;    }

    public void setsem(String semester) {
        this.semester = semester;
    }

}

