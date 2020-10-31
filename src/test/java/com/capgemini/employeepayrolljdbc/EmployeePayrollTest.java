package com.capgemini.employeepayrolljdbc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

import com.capgemini.employeepayrolldata.EmployeePayrollData;
import com.capgemini.exception.DatabaseException;

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

}