package com.capgemini.employeepayrolljdbc;

import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

import com.capgemini.employeepayrolldata.EmployeePayrollData;
import com.capgemini.exception.DatabaseException;

public class EmployeePayrollTest {
	
	@Test
	public void givenEmployeePayrollDB_ShouldAssertNumberOfEntries() throws DatabaseException {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		ArrayList<EmployeePayrollData> list =  employeePayrollDBService.readEmployeeDB();
		assertEquals(3, list.size());
	}

}
