package com.capgemini.employeepayrolljdbc;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

import com.capgemini.employeepayrolldata.EmployeePayrollData;
import com.capgemini.exception.DatabaseException;
import com.sun.tools.javac.util.List;

public class EmployeePayrollTest {

	@Test
	public void givenEmployeePayrollDB_ShouldAssertNumberOfEntries() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		ArrayList<EmployeePayrollData> list = employeePayrollDBService.readEmployeeDB();
		assertEquals(4, list.size());
	}

	@Test
	public void givenEmployeePayollDB_shouldPerformUpdate() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		int resultSetNo = employeePayrollDBService.updateQuery("Terisa", 3000000);
		System.out.println(resultSetNo);
		assertEquals(1, resultSetNo);
	}

	@Test
	public void givenEmployeePayollDB_shouldPerformUpdateUsingPreparedStatement() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		int resultSetNo = employeePayrollDBService.updateQueryUsingPreparedStatement("Terisa", 4000000);
		System.out.println(resultSetNo);
		assertEquals(1, resultSetNo);
	}

	@Test
	public void givenEmployeePayrollDB_findNumberOfEmployeeWithinGivenDateRange() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		ArrayList<EmployeePayrollData> list = employeePayrollDBService
				.employeeDataWithinGivenDateRange(LocalDate.of(2019, 01, 01), LocalDate.of(2020, 10, 30));
		assertEquals(3, list.size());
	}
	
	@Test
	public void givenEmployeePayollDB_shouldPerformSumOfSalaryByGender() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		double sum = employeePayrollDBService.sumOfSalaryGroupByGender("M");
		assertEquals(6000000.00, sum, 0.0);
	}
	
	@Test
	public void givenEmployeePayollDB_addNewEmployeeToDB() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		employeePayrollDBService.addEmployeeToPayroll("Akansha", "F", 1250000.00, LocalDate.now());
	}
	
	@Test
	public void givenPayrollDB_recordTimeToAddEmployee() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		Instant start = Instant.now();
		employeePayrollDBService.addEmployeeToPayroll("Charlie", "M", 3000000, LocalDate.of(2020, 05, 21));
		employeePayrollDBService.addEmployeeToPayroll("Terisa", "F", 3000000, LocalDate.of(2020, 10, 13));
		employeePayrollDBService.addEmployeeToPayroll("Akansha", "F", 1250000, LocalDate.now());
		Instant end = Instant.now();
		System.out.println("Duration for entering the data using console is " +Duration.between(start, end));
		
		ArrayList<EmployeePayrollData> employeeList = new ArrayList<EmployeePayrollData>();
		employeeList.add(new EmployeePayrollData(6, "Charlie", "M", 3000000, LocalDate.of(2020, 05, 21)));
		employeeList.add(new EmployeePayrollData(7, "Terisa", "F", 3000000, LocalDate.of(2020, 10, 13)));
		employeeList.add(new EmployeePayrollData(8, "Akansha", "F", 1250000, LocalDate.now()));
		Instant threadStart = Instant.now();
		employeePayrollDBService.addEmployeeDataUsingThread(employeeList);
		Instant threadEnd = Instant.now();
		System.out.println("Duration for complete execution with Threads: " + Duration.between(threadStart, threadEnd));
	}
	
}