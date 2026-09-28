package Services;

import DAO.Attendance_DAO;
import DAO.Student_DAO;
import DAO.Course_DAO;
import Model.Attendance;
import Model.Course;
import Exception.DuplicateAttendance_Exception;
import Exception.StudentNotFound_Exception;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Attendance_Service {

    private Attendance_DAO attendanceDAO;
    private Student_DAO studentDAO;
    private Course_DAO courseDAO;

    public Attendance_Service(Attendance_DAO attendanceDAO, Student_DAO studentDAO, Course_DAO courseDAO) {
        this.attendanceDAO = attendanceDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    public void markAttendance(int studentId, int courseId, LocalDate date, Attendance.Status status)
            throws SQLException, DuplicateAttendance_Exception, StudentNotFound_Exception {

        // Student exist garcha ki check garne (yesले StudentNotFoundException throw garcha)
        studentDAO.getStudentById(studentId);

        // Course exist garcha ki check garne
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            throw new StudentNotFound_Exception("Course with ID " + courseId + " does not exist.");
        }

        // Duplicate check
        if (attendanceDAO.isAlreadyMarked(studentId, courseId, date)) {
            throw new DuplicateAttendance_Exception(
                    "Attendance already marked for student " + studentId +
                            " in course " + courseId + " on " + date);
        }

        Attendance attendance = new Attendance(0, studentId, courseId, date, status);
        attendanceDAO.markAttendance(attendance);
    }

    public List<Attendance> getHistoryForStudent(int studentId) throws SQLException {
        return attendanceDAO.getHistoryByStudent(studentId);
    }

    public List<Attendance> getHistoryForCourse(int courseId) throws SQLException {
        return attendanceDAO.getHistoryByCourse(courseId);
    }

    public double calculateAttendancePercentage(int studentId, int courseId) throws SQLException {
        int total = attendanceDAO.countTotal(studentId, courseId);
        if (total == 0) return 0.0;

        int present = attendanceDAO.countPresent(studentId, courseId);
        return (present * 100.0) / total;
    }
}