
-- Student Management System - Database Schema
-- Database: sms_db
CREATE DATABASE IF NOT EXISTS sms_db;
USE sms_db;
-- Drop tables if they already exist (fresh setup)
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;
-- Students table
-- student_id is entered manually by the user (NOT auto-increment)
CREATE TABLE students (
                          student_id INT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100),
                          phone VARCHAR(20)
);
-- Courses table
-- course_id is auto-generated
CREATE TABLE courses (
                         course_id INT AUTO_INCREMENT PRIMARY KEY,
                         course_name VARCHAR(100) NOT NULL,
                         course_code VARCHAR(20) UNIQUE NOT NULL
);
-- Enrollments table (links a student to a course, stores marks)
CREATE TABLE enrollments (
                             enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
                             student_id INT NOT NULL,
                             course_id INT NOT NULL,
                             marks DOUBLE DEFAULT NULL,
                             FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
                             FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
                             UNIQUE (student_id, course_id)
);
-- Attendance table
CREATE TABLE attendance (
                            attendance_id INT AUTO_INCREMENT PRIMARY KEY,
                            student_id INT NOT NULL,
                            course_id INT NOT NULL,
                            attendance_date DATE NOT NULL,
                            status ENUM('PRESENT','ABSENT') NOT NULL,
                            FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
                            FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
                            UNIQUE (student_id, course_id, attendance_date)
);