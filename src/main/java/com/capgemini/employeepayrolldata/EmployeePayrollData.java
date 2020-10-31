package com.capgemini.employeepayrolldata;

import java.time.LocalDate;

public class EmployeePayrollData {
	
	private int empId;
	private String empName;
	private double salary;
	private LocalDate start;
	private String gender;
	
	public EmployeePayrollData(int empId, String empName, String gender, double salary, LocalDate start) {
		this.setEmpId(empId);
		this.setEmpName(empName);
		this.setSalary(salary);
		this.setStart(start);
		this.setGender(gender);
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	@Override
	public String toString() {
		return "ID: " + empId + "Emp Name: "+empName+"Salary: Rs." +salary;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
