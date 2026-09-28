package DAO;

import Database.DBConnection;
import Model.Attendance;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Attendance_DAO {

    // Mark attendance
    public void markAttendance(Attendance attendance) throws SQLException {
        String sql = "INSERT INTO attendance (student_id, course_id, attendance_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, attendance.getStudentId());
            ps.setInt(2, attendance.getCourseId());
            ps.setDate(3, Date.valueOf(attendance.getDate()));
            ps.setString(4, attendance.getStatus().name());
            ps.executeUpdate();
        }
    }

    // Check if already marked for that student+course+date (used to prevent duplicates)
    public boolean isAlreadyMarked(int studentId, int courseId, LocalDate date) throws SQLException {
        String sql = "SELECT 1 FROM attendance WHERE student_id = ? AND course_id = ? AND attendance_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setDate(3, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Full attendance history for a student (optionally filter by course)
    public List<Attendance> getHistoryByStudent(int studentId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ? ORDER BY attendance_date";

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

    // All attendance records for a course (for reports)
    public List<Attendance> getHistoryByCourse(int courseId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE course_id = ? ORDER BY attendance_date";

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

    // Count present days for a student in a course (for percentage calc)
    public int countPresent(int studentId, int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course_id = ? AND status = 'PRESENT'";
        return count(sql, studentId, courseId);
    }

    // Count total marked days for a student in a course
    public int countTotal(int studentId, int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course_id = ?";
        return count(sql, studentId, courseId);
    }

    private int count(String sql, int studentId, int courseId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private Attendance mapRow(ResultSet rs) throws SQLException {
        return new Attendance(
                rs.getInt("attendance_id"),
                rs.getInt("student_id"),
                rs.getInt("course_id"),
                rs.getDate("attendance_date").toLocalDate(),
                Attendance.Status.valueOf(rs.getString("status"))
        );
    }
}