import DAO.Attendance_DAO;
import DAO.Course_DAO;
import DAO.Enrollment_DAO;
import DAO.Student_DAO;
import Exception.DuplicateAttendance_Exception;
import Exception.StudentNotFound_Exception;
import Model.Attendance;
import Model.Course;
import Model.Student;
import Services.Attendance_Service;
import Services.Course_Service;
import Services.Student_Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final Student_DAO studentDAO = new Student_DAO();
    private static final Course_DAO courseDAO = new Course_DAO();
    private static final Enrollment_DAO enrollmentDAO = new Enrollment_DAO();
    private static final Attendance_DAO attendanceDAO = new Attendance_DAO();

    private static final Student_Service studentService = new Student_Service(studentDAO);
    private static final Course_Service courseService = new Course_Service(courseDAO, enrollmentDAO, studentDAO);
    private static final Attendance_Service attendanceService = new Attendance_Service(attendanceDAO, studentDAO, courseDAO);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> studentMenu();
                case 2 -> courseMenu();
                case 3 -> attendanceMenu();
                case 4 -> reportsMenu();
                case 5 -> {
                    running = false;
                    System.out.println("Byeee!");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    private static void printMainMenu() {
        System.out.println("\nWELCOME TO STUDENT MANAGEMENT SYSTEM");
        System.out.println("1. Student Management");
        System.out.println("2. Course & Enrollment Management");
        System.out.println("3. Attendance Management");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
    }

    private static void studentMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\nStudent Management");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by Name");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Back");

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        int id = readInt("Student id: ");
                        String name = readString("Name: ");
                        String email = readString("Email: ");
                        String phone = readString("Phone: ");
                        studentService.addStudent(id,name, email, phone);
                        System.out.println("Student added successfully.");
                    }
                    case 2 -> {
                        List<Student> students = studentService.getAllStudents();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            for (Student s : students) s.displayInfo();
                        }
                    }
                    case 3 -> {
                        String name = readString("Enter name to search: ");
                        List<Student> results = studentService.searchByName(name);
                        if (results.isEmpty()) {
                            System.out.println("No matching students.");
                        } else {
                            for (Student s : results) s.displayInfo();
                        }
                    }
                    case 4 -> {
                        int id = readInt("Student ID to update: ");
                        String name = readString("New name: ");
                        String email = readString("New email: ");
                        String phone = readString("New phone: ");
                        studentService.updateStudent(id, name, email, phone);
                        System.out.println("Student updated.");
                    }
                    case 5 -> {
                        int id = readInt("Student ID to delete: ");
                        studentService.deleteStudent(id);
                        System.out.println("Student deleted.");
                    }
                    case 6 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (StudentNotFound_Exception e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static void courseMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\nCourse & Enrollment");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Enroll Student in Course");
            System.out.println("4. Unenroll Student from Course");
            System.out.println("5. Record Marks");
            System.out.println("6. List Students in a Course");
            System.out.println("7. Back");

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        String name = readString("Course name: ");
                        String code = readString("Course code: ");
                        courseService.addCourse(name, code);
                        System.out.println("Course added.");
                    }
                    case 2 -> {
                        List<Course> courses = courseService.getAllCourses();
                        if (courses.isEmpty()) {
                            System.out.println("No courses found.");
                        } else {
                            for (Course c : courses) System.out.println(c);
                        }
                    }
                    case 3 -> {
                        int studentId = readInt("Student ID: ");
                        int courseId = readInt("Course ID: ");
                        courseService.enrollStudent(studentId, courseId);
                        System.out.println("Enrollment processed.");
                    }
                    case 4 -> {
                        int studentId = readInt("Student ID: ");
                        int courseId = readInt("Course ID: ");
                        courseService.unenrollStudent(studentId, courseId);
                        System.out.println("Unenrolled successfully.");
                    }
                    case 5 -> {
                        int studentId = readInt("Student ID: ");
                        int courseId = readInt("Course ID: ");
                        double marks = readDouble("Marks: ");
                        courseService.recordMarks(studentId, courseId, marks);
                        System.out.println("Marks recorded.");
                    }
                    case 6 -> {
                        int courseId = readInt("Course ID: ");
                        List<Student> students = courseService.getStudentsByCourse(courseId);
                        if (students.isEmpty()) {
                            System.out.println("No students enrolled in this course.");
                        } else {
                            for (Student s : students) s.displayInfo();
                        }
                    }
                    case 7 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (StudentNotFound_Exception e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static void attendanceMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\nAttendance Management");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Attendance History (by student)");
            System.out.println("3. View Attendance % (student in a course)");
            System.out.println("4. Back");

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        int studentId = readInt("Student ID: ");
                        int courseId = readInt("Course ID: ");
                        LocalDate date = readDate("Date (yyyy-MM-dd): ");
                        String statusStr = readString("Status (PRESENT/ABSENT): ").toUpperCase();

                        Attendance.Status status;
                        try {
                            status = Attendance.Status.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid status. Must be PRESENT or ABSENT.");
                            continue;
                        }

                        attendanceService.markAttendance(studentId, courseId, date, status);
                        System.out.println("Attendance marked.");
                    }
                    case 2 -> {
                        int studentId = readInt("Student ID: ");
                        List<Attendance> history = attendanceService.getHistoryForStudent(studentId);
                        if (history.isEmpty()) {
                            System.out.println("No attendance records found.");
                        } else {
                            for (Attendance a : history) System.out.println(a);
                        }
                    }
                    case 3 -> {
                        int studentId = readInt("Student ID: ");
                        int courseId = readInt("Course ID: ");
                        double percentage = attendanceService.calculateAttendancePercentage(studentId, courseId);
                        System.out.printf("Attendance: %.2f%%\n", percentage);
                    }
                    case 4 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (DuplicateAttendance_Exception e) {
                System.out.println("Error: " + e.getMessage());
            } catch (StudentNotFound_Exception e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static void reportsMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\nReports");
            System.out.println("1. Class Ranking (by average marks)");
            System.out.println("2. Back");

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        List<Student> ranked = courseService.getClassRanking();
                        int rank = 1;
                        for (Student s : ranked) {
                            System.out.printf("#%d - %s (Avg: %.2f)\n", rank++, s.getName(), s.getAverageMarks());
                        }
                    }
                    case 2 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use yyyy-MM-dd (e.g. 2026-09-26).");
            }
        }
    }
}