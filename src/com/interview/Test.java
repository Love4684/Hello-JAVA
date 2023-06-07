package com.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        Employee emp3 = new Employee();
        Employee emp4 = new Employee();

        emp1.setEmpId(100);
        emp1.setEmpName("Rahul");
        emp1.setSalary(1000);
        emp1.setGender(Gender.Male);

        emp2.setEmpId(101);
        emp2.setEmpName("Ram");
        emp2.setSalary(2000);
        emp2.setGender(Gender.Male);

        emp3.setEmpId(102);
        emp3.setEmpName("Shyam");
        emp3.setSalary(3000);
        emp3.setGender(Gender.Male);

        emp4.setEmpId(103);
        emp4.setEmpName("ajay");
        emp4.setSalary(4000);
        emp4.setGender(Gender.Male);

        List<Employee> employeeList = new ArrayList<>();

        employeeList.add(emp1);
        employeeList.add(emp2);
        employeeList.add(emp3);
        employeeList.add(emp4);

        List<Employee> list = employeeList.stream().filter(e->e.getSalary() > 1000).collect(Collectors.toList());

        System.out.println(list);
    }
}
