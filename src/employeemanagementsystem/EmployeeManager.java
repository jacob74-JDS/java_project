/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employeemanagementsystem;

/**
 *
 * @author yakob
 */
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeManager {
    private List<Employee> employees;
    private static final String DATA_FILE = "employees.dat";

    public EmployeeManager() {
        employees = new ArrayList<>();
        loadEmployees();
    }

    // CRUD Operations
    public void addEmployee(Employee employee) throws IllegalArgumentException {
        if (findEmployeeById(employee.getEmployeeId()) != null) {
            throw new IllegalArgumentException("Employee ID already exists.");
        }
        employees.add(employee);
        saveEmployees();
    }

    public Employee findEmployeeById(int employeeId) {
        return employees.stream()
                .filter(e -> e.getEmployeeId() == employeeId)
                .findFirst()
                .orElse(null);
    }

    public boolean updateEmployee(int employeeId, String fullName, Integer age, 
                                 String department, String position, Double salary) {
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            return false;
        }

        if (fullName != null) employee.setFullName(fullName);
        if (age != null) employee.setAge(age);
        if (department != null) employee.setDepartment(department);
        if (position != null) employee.setPosition(position);
        if (salary != null) employee.setSalary(salary);

        saveEmployees();
        return true;
    }

    public boolean deleteEmployee(int employeeId) {
        Employee employee = findEmployeeById(employeeId);
        if (employee != null) {
            employees.remove(employee);
            saveEmployees();
            return true;
        }
        return false;
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    // Search Operations
    public List<Employee> searchEmployeesByName(String name) {
        return employees.stream()
                .filter(e -> e.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Employee> searchEmployeesByDepartment(String department) {
        return employees.stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public List<Employee> searchEmployeesByPosition(String position) {
        return employees.stream()
                .filter(e -> e.getPosition().equalsIgnoreCase(position))
                .collect(Collectors.toList());
    }

    // Sorting Operations
    public List<Employee> sortEmployeesByName() {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getFullName))
                .collect(Collectors.toList());
    }

    public List<Employee> sortEmployeesByDepartment() {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getDepartment))
                .collect(Collectors.toList());
    }

    public List<Employee> sortEmployeesBySalary() {
        return employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .collect(Collectors.toList());
    }

    // Statistics
    public Map<String, Double> getAverageSalaryByDepartment() {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)
                ));
    }

    // Data Persistence
    private void saveEmployees() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            System.err.println("Error saving employee data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadEmployees() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                employees = (List<Employee>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading employee data: " + e.getMessage());
            }
        }
    }
}