package Model;

public class Student extends Person {

    private double averageMarks;

    public Student(int id, String name, String email, String phone) {
        super(id, name, email, phone);
        this.averageMarks = 0.0;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    // Polymorphism - Person ko abstract method override garyo
    @Override
    public void displayInfo() {
        System.out.println("Student ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + getPhone());
        System.out.println("Average Marks: " + averageMarks);
    }
}