/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employeemanagementsystem;

/**
 *
 * @author yakob
 */
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static EmployeeManager employeeManager = new EmployeeManager();

    public static void main(String[] args) {
        // Simple authentication
        if (!authenticate()) {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployee();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    listAllEmployees();
                    break;
                case 6:
                    searchEmployees();
                    break;
                case 7:
                    sortEmployees();
                    break;
                case 8:
                    displayStatistics();
                    break;
                case 9:
                    running = false;
                    System.out.println("Exiting Employee Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static boolean authenticate() {
        System.out.println("=== Employee Management System Login ===");
        String username = getStringInput("Username: ");
        String password = getStringInput("Password: ");
        
        // Simple hardcoded credentials for demonstration
        return "admin".equals(username) && "password123".equals(password);
    }

    private static void displayMenu() {
        System.out.println("\n=== Employee Management System ===");
        System.out.println("1. Add New Employee");
        System.out.println("2. View Employee Details");
        System.out.println("3. Update Employee Information");
        System.out.println("4. Delete Employee Record");
        System.out.println("5. List All Employees");
        System.out.println("6. Search Employees");
        System.out.println("7. Sort Employees");
        System.out.println("8. Display Statistics");
        System.out.println("9. Exit");
    }

    private static void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        int employeeId = getIntInput("Employee ID: ");
        
        // Check if employee ID already exists
        if (employeeManager.findEmployeeById(employeeId) != null) {
            System.out.println("Error: Employee ID already exists.");
            return;
        }

        String fullName = getStringInput("Full Name: ");
        int age = getIntInput("Age: ");
        String department = getStringInput("Department: ");
        String position = getStringInput("Position: ");
        double salary = getDoubleInput("Salary: ");

        try {
            employeeManager.addEmployee(new Employee(employeeId, fullName, age, department, position, salary));
            System.out.println("Employee added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewEmployee() {
        System.out.println("\n--- View Employee Details ---");
        int employeeId = getIntInput("Enter Employee ID: ");
        Employee employee = employeeManager.findEmployeeById(employeeId);

        if (employee != null) {
            displayEmployeeHeader();
            System.out.println(employee);
        } else {
            System.out.println("Employee not found with ID: " + employeeId);
        }
    }

    private static void updateEmployee() {
        System.out.println("\n--- Update Employee Information ---");
        int employeeId = getIntInput("Enter Employee ID: ");
        Employee employee = employeeManager.findEmployeeById(employeeId);

        if (employee == null) {
            System.out.println("Employee not found with ID: " + employeeId);
            return;
        }

        System.out.println("Current Employee Details:");
        displayEmployeeHeader();
        System.out.println(employee);

        System.out.println("\nEnter new details (leave blank to keep current value):");
        String fullName = getStringInput("Full Name [" + employee.getFullName() + "]: ", true);
        Integer age = getIntInputOrNull("Age [" + employee.getAge() + "]: ");
        String department = getStringInput("Department [" + employee.getDepartment() + "]: ", true);
        String position = getStringInput("Position [" + employee.getPosition() + "]: ", true);
        Double salary = getDoubleInputOrNull("Salary [" + employee.getSalary() + "]: ");

        boolean updated = employeeManager.updateEmployee(
                employeeId,
                fullName.isEmpty() ? null : fullName,
                age,
                department.isEmpty() ? null : department,
                position.isEmpty() ? null : position,
                salary
        );

        if (updated) {
            System.out.println("Employee updated successfully!");
        } else {
            System.out.println("Failed to update employee.");
        }
    }

    private static void deleteEmployee() {
        System.out.println("\n--- Delete Employee Record ---");
        int employeeId = getIntInput("Enter Employee ID to delete: ");
        boolean deleted = employeeManager.deleteEmployee(employeeId);

        if (deleted) {
            System.out.println("Employee deleted successfully!");
        } else {
            System.out.println("Employee not found with ID: " + employeeId);
        }
    }

    private static void listAllEmployees() {
        System.out.println("\n--- List of All Employees ---");
        List<Employee> employees = employeeManager.getAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        displayEmployeeHeader();
        employees.forEach(System.out::println);
    }

    private static void searchEmployees() {
        System.out.println("\n--- Search Employees ---");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Department");
        System.out.println("3. Search by Position");
        int choice = getIntInput("Enter search option: ");

        List<Employee> results;
        switch (choice) {
            case 1:
                String name = getStringInput("Enter name to search: ");
                results = employeeManager.searchEmployeesByName(name);
                break;
            case 2:
                String department = getStringInput("Enter department to search: ");
                results = employeeManager.searchEmployeesByDepartment(department);
                break;
            case 3:
                String position = getStringInput("Enter position to search: ");
                results = employeeManager.searchEmployeesByPosition(position);
                break;
            default:
                System.out.println("Invalid search option.");
                return;
        }

        if (results.isEmpty()) {
            System.out.println("No employees found matching the criteria.");
        } else {
            System.out.println("\nSearch Results:");
            displayEmployeeHeader();
            results.forEach(System.out::println);
        }
    }

    private static void sortEmployees() {
        System.out.println("\n--- Sort Employees ---");
        System.out.println("1. Sort by Name");
        System.out.println("2. Sort by Department");
        System.out.println("3. Sort by Salary (High to Low)");
        int choice = getIntInput("Enter sort option: ");

        List<Employee> sortedEmployees;
        switch (choice) {
            case 1:
                sortedEmployees = employeeManager.sortEmployeesByName();
                break;
            case 2:
                sortedEmployees = employeeManager.sortEmployeesByDepartment();
                break;
            case 3:
                sortedEmployees = employeeManager.sortEmployeesBySalary();
                break;
            default:
                System.out.println("Invalid sort option.");
                return;
        }

        System.out.println("\nSorted Employees:");
        displayEmployeeHeader();
        sortedEmployees.forEach(System.out::println);
    }

    private static void displayStatistics() {
        System.out.println("\n--- Employee Statistics ---");
        Map<String, Double> avgSalaries = employeeManager.getAverageSalaryByDepartment();

        if (avgSalaries.isEmpty()) {
            System.out.println("No employee data available for statistics.");
            return;
        }

        System.out.println("\nAverage Salaries by Department:");
        System.out.printf("%-15s %-15s%n", "Department", "Average Salary");
        System.out.println("-------------------------------");
        avgSalaries.forEach((dept, avg) -> 
            System.out.printf("%-15s $%,.2f%n", dept, avg));
    }

    private static void displayEmployeeHeader() {
        System.out.printf("%-10s %-20s %-5s %-15s %-15s %-10s%n",
                "ID", "Name", "Age", "Department", "Position", "Salary");
        System.out.println("----------------------------------------------------------------");
    }

    // Utility methods for input handling
    private static String getStringInput(String prompt) {
        return getStringInput(prompt, false);
    }

    private static String getStringInput(String prompt, boolean allowEmpty) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (!allowEmpty && input.isEmpty()) {
            System.out.println("This field cannot be empty. Please try again.");
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static Integer getIntInputOrNull(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer or leave blank.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static Double getDoubleInputOrNull(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or leave blank.");
            }
        }
    }
}