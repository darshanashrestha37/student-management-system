package Model;

public class Enrollment {

    private int id;
    private int studentId;
    private int courseId;
    private Double marks;   // null huna sakcha (marks navako bela)

    public Enrollment(int id, int studentId, int courseId, Double marks) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
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

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Enrollment{studentId=" + studentId + ", courseId=" + courseId + ", marks=" + marks + "}";
    }
}