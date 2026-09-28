package DAO;

import Database.DBConnection;
import Model.Enrollment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Enrollment_DAO {

    // Enroll a student in a course
    public void enroll(int studentId, int courseId) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        }
    }

    // Unenroll
    public void unenroll(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        }
    }

    // Update marks for a student in a course
    public boolean updateMarks(int studentId, int courseId, double marks) throws SQLException {
        String sql = "UPDATE enrollments SET marks = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, marks);
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);

            int rows = ps.executeUpdate();
            return rows > 0;   // true bhaye matra kehi update bhayo
        }
    }

    // Get all enrollments for a student (used for average calculation)
    public List<Enrollment> getEnrollmentsByStudent(int studentId) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // Get all students enrolled in a course
    public List<Enrollment> getEnrollmentsByCourse(int courseId) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // Check if already enrolled (used before enroll())
    public boolean isEnrolled(int studentId, int courseId) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        double marksVal = rs.getDouble("marks");
        Double marks = rs.wasNull() ? null : marksVal;

        return new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getInt("student_id"),
                rs.getInt("course_id"),
                marks
        );
    }
}
