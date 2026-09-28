package Model;

import java.time.LocalDate;

public class Attendance {

    public enum Status {
        PRESENT,
        ABSENT
    }

    private int id;
    private int studentId;
    private int courseId;
    private LocalDate date;
    private Status status;

    public Attendance(int id, int studentId, int courseId, LocalDate date, Status status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return date + " - Student:" + studentId + " Course:" + courseId + " -> " + status;
    }
}