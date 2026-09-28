package Services;

import DAO.Course_DAO;
import DAO.Enrollment_DAO;
import DAO.Student_DAO;
import Model.Course;
import Model.Enrollment;
import Model.Student;
import Exception.StudentNotFound_Exception;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Course_Service {

    private Course_DAO courseDAO;
    private Enrollment_DAO enrollmentDAO;
    private Student_DAO studentDAO;

    public Course_Service(Course_DAO courseDAO, Enrollment_DAO enrollmentDAO, Student_DAO studentDAO) {
        this.courseDAO = courseDAO;
        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
    }

    public void addCourse(String name, String code) throws SQLException {
        Course course = new Course(0, name, code);
        courseDAO.addCourse(course);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public void deleteCourse(int courseId) throws SQLException {
        courseDAO.deleteCourse(courseId);
    }

    // Enroll - business rule: don't allow duplicate enrollment
    public void enrollStudent(int studentId, int courseId) throws SQLException, StudentNotFound_Exception {
        // Pahila check garne: student exist garcha ki?
        studentDAO.getStudentById(studentId);  // yesले StudentNotFoundException throw garcha bhettiena bhane

        // Course exist garcha ki check garne
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            throw new StudentNotFound_Exception("Course with ID " + courseId + " does not exist.");
        }

        // Duplicate enrollment check
        if (enrollmentDAO.isEnrolled(studentId, courseId)) {
            System.out.println("Student is already enrolled in this course.");
            return;
        }

        enrollmentDAO.enroll(studentId, courseId);
    }

    public void unenrollStudent(int studentId, int courseId) throws SQLException {
        enrollmentDAO.unenroll(studentId, courseId);
    }

    public void recordMarks(int studentId, int courseId, double marks) throws SQLException, StudentNotFound_Exception {
        boolean updated = enrollmentDAO.updateMarks(studentId, courseId, marks);
        if (!updated) {
            throw new StudentNotFound_Exception(
                    "Student " + studentId + " is not enrolled in course " + courseId + ". Cannot record marks.");
        }
    }

    // Compute average marks of a student across all enrolled courses
    public double calculateAverage(int studentId) throws SQLException {
        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudent(studentId);

        double sum = 0;
        int count = 0;
        for (Enrollment e : enrollments) {
            if (e.getMarks() != null) {
                sum += e.getMarks();
                count++;
            }
        }
        return count == 0 ? 0.0 : sum / count;
    }

    // List all students enrolled in a specific course
    public List<Student> getStudentsByCourse(int courseId) throws SQLException, StudentNotFound_Exception {
        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByCourse(courseId);
        List<Student> students = new ArrayList<>();

        for (Enrollment e : enrollments) {
            students.add(studentDAO.getStudentById(e.getStudentId()));
        }
        return students;
    }

    // Class ranking report - students sorted by average marks descending
    public List<Student> getClassRanking() throws SQLException {
        List<Student> students = studentDAO.getAllStudents();

        for (Student s : students) {
            try {
                s.setAverageMarks(calculateAverage(s.getId()));
            } catch (SQLException e) {
                s.setAverageMarks(0.0);
            }
        }

        // Sorting using Comparator (collections requirement)
        students.sort(Comparator.comparingDouble(Student::getAverageMarks).reversed());
        return students;
    }
}