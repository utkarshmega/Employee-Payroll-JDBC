package com.capgemini.employeepayrolljdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.capgemini.employeepayrolldata.EmployeePayrollData;
import com.capgemini.exception.DatabaseException;
import com.capgemini.exception.DatabaseException.ExceptionType;

public class EmployeePayrollDBService {

	public ArrayList<EmployeePayrollData> readEmployeeDB() throws DatabaseException
	{
		ArrayList<EmployeePayrollData> list = new ArrayList<>();
		String query = "Select * from employee_detail";
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next())
			{
				int id = resultSet.getInt("id");
				String emp_name = resultSet.getString("Employee_name");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				System.out.println("Employee Name: "+emp_name);
				list.add(new EmployeePayrollData(id, emp_name, salary, startDate));
			}
			return list;
		}
		catch(SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	private Connection getConnection() throws DatabaseException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll?useSSL=false";
		String username = "root";
		String password = "sqldatabase@1252";
		Connection connection;
		try {
			connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connection Sucessful!!" +connection);
		} catch(SQLException e) {
			throw new DatabaseException("Unable to connect to database", ExceptionType.UNABLE_TO_CONNECT);
		}
		
		return connection;
	}

}
