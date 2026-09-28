package DAO;

import Database.DBConnection;
import Model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course_DAO {

    public void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (course_name, course_code) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getCourseCode());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    course.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                courses.add(mapRow(rs));
            }
        }
        return courses;
    }

    public Course getCourseById(int id) throws SQLException {
        String sql = "SELECT * FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE courses SET course_name = ?, course_code = ? WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getCourseCode());
            ps.setInt(3, course.getId());
            ps.executeUpdate();
        }
    }

    public void deleteCourse(int id) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        return new Course(
                rs.getInt("course_id"),
                rs.getString("course_name"),
                rs.getString("course_code")
        );
    }
}