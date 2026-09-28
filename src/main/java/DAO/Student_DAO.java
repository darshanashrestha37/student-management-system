package DAO;

import Database.DBConnection;
import Model.Student;
import Exception.StudentNotFound_Exception;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Student_DAO {

    // CREATE
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.executeUpdate();
        }
    }

    // Check if a student ID already exists (used to prevent duplicate manual IDs)
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // READ - all students
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }
        }
        return students;
    }

    // READ - by id
    public Student getStudentById(int id) throws SQLException, StudentNotFound_Exception {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                } else {
                    throw new StudentNotFound_Exception("No student found with ID: " + id);
                }
            }
        }
    }

    // READ - search by name (partial match)
    public List<Student> searchByName(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
        }
        return students;
    }

    // UPDATE
    public void updateStudent(Student student) throws SQLException, StudentNotFound_Exception {
        String sql = "UPDATE students SET name = ?, email = ?, phone = ? WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setInt(4, student.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new StudentNotFound_Exception("No student found with ID: " + student.getId());
            }
        }
    }

    // DELETE
    public void deleteStudent(int id) throws SQLException, StudentNotFound_Exception {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new StudentNotFound_Exception("No student found with ID: " + id);
            }
        }
    }

    // Helper - ResultSet row -> Student object
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student(
                rs.getInt("student_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );
        return s;
    }
}