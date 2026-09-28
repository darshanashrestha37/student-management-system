package Services;

import DAO.Student_DAO;
import Model.Student;
import Exception.StudentNotFound_Exception;

import java.sql.SQLException;
import java.util.List;

public class Student_Service {

    private Student_DAO studentDAO;

    public Student_Service(Student_DAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void addStudent(int id, String name, String email, String phone) throws SQLException {
        if (studentDAO.existsById(id)) {
            System.out.println("Student ID " + id + " already exists. Choose a different ID.");
            return;
        }
        Student student = new Student(id, name, email, phone);
        studentDAO.addStudent(student);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public Student getStudentById(int id) throws SQLException, StudentNotFound_Exception {
        return studentDAO.getStudentById(id);
    }

    public List<Student> searchByName(String name) throws SQLException {
        return studentDAO.searchByName(name);
    }

    public void updateStudent(int id, String name, String email, String phone)
            throws SQLException, StudentNotFound_Exception {
        Student student = new Student(id, name, email, phone);
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int id) throws SQLException, StudentNotFound_Exception {
        studentDAO.deleteStudent(id);
    }
}