package com.capgemini.employeepayrolljdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.capgemini.employeepayrolldata.EmployeePayrollData;
import com.capgemini.exception.DatabaseException;
import com.capgemini.exception.DatabaseException.ExceptionType;

public class EmployeePayrollDBService {

	public ArrayList<EmployeePayrollData> readEmployeeDB() throws DatabaseException {
		ArrayList<EmployeePayrollData> list = new ArrayList<>();
		String query = "Select * from employee_detail";
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String emp_name = resultSet.getString("Employee_name");
				String gender = resultSet.getString("Gender");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				System.out.println("Employee Name: " + emp_name + " Salary: Rs. " + salary);
				list.add(new EmployeePayrollData(id, emp_name, gender, salary, startDate));
			}
			return list;
		} catch (SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	public int updateQuery(String name, double salary) throws DatabaseException {

		String query = String.format("update employee_detail set salary = %.2f where employee_name = '%s';", salary,
				name);

		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			return statement.executeUpdate(query);
		} catch (SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	public int updateQueryUsingPreparedStatement(String name, double salary) throws DatabaseException {

		String query = String.format("update employee_detail set salary = ? where employee_name = ?;");

		try {
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(2, name);
			preparedStatement.setDouble(1, salary);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	public ArrayList<EmployeePayrollData> employeeDataWithinGivenDateRange(LocalDate start, LocalDate end)
			throws DatabaseException {
		ArrayList<EmployeePayrollData> list = new ArrayList<>();
		String query = String.format("Select * from employee_detail where START BETWEEN '%s' AND '%s';",
				Date.valueOf(start), Date.valueOf(end));
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String emp_name = resultSet.getString("Employee_name");
				String gender = resultSet.getString("Gender");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				System.out.println("Employee Name: " + emp_name + " Salary: Rs. " + salary);
				list.add(new EmployeePayrollData(id, emp_name, gender, salary, startDate));
			}
			return list;
		} catch (SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	public double sumOfSalaryGroupByGender(String gender) throws DatabaseException {

		String query = String.format("select SUM(salary) from employee_detail where gender = '%s' group by gender;",
				gender);

		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			double sum = 0;
			while (resultSet.next()) {
				double salary = resultSet.getDouble("SUM(salary)");
				sum += salary;
			}
			System.out.println("Sum of salary group by " + gender + " is Rs. " + sum);
			return sum;
		} catch (SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate start) throws DatabaseException {
		String query = String.format(
				"Insert into employee_detail(employee_name, gender, salary, start) values ('%s', '%s', '%s', '%s');", name,
				gender, salary, Date.valueOf(start));
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			int resultSet = statement.executeUpdate(query);
			if(resultSet == 1)
				System.out.println("New data added to the database");
			else
				System.out.println("Data not added to the database");
		} catch(SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
		readEmployeeDB();
	}

	private Connection getConnection() throws DatabaseException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll?useSSL=false";
		String username = "root";
		String password = "sqldatabase@1252";
		Connection connection;
		try {
			connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connection Sucessful!!" + connection);
		} catch (SQLException e) {
			throw new DatabaseException("Unable to connect to database", ExceptionType.UNABLE_TO_CONNECT);
		}

		return connection;
	}

}
