package Model;

public class Course {

    private int id;
    private String courseName;
    private String courseCode;

    public Course(int id, String courseName, String courseCode) {
        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}