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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate start)
			throws DatabaseException {
		String query = String.format(
				"Insert into employee_detail(employee_name, gender, salary, start) values ('%s', '%s', '%s', '%s');",
				name, gender, salary, Date.valueOf(start));
		Connection connection = null;
		try {
			connection = getConnection();
			Statement statement = connection.createStatement();
			int resultSet = statement.executeUpdate(query);
			if (resultSet == 1)
				System.out.println("New data added to the database");
			else
				System.out.println("Data not added to the database");
		} catch (SQLException e) {
			throw new DatabaseException("Unable to execute query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
		readEmployeeDB();

		try {
			Statement statement = connection.createStatement();
			double deduction = 0.20 * salary;
			double taxable_pay = salary - deduction;
			double tax = 0.10 * taxable_pay;
			double net_pay = salary - tax;

			String query1 = String.format(
					"Insert into payroll_detail (employee_name, deduction, taxable_salary, tax, net_pay) values ('%s', '%s', '%s', '%s', '%s');",
					name, deduction, taxable_pay, tax, net_pay);
			int result = statement.executeUpdate(query1);
			if (result == 1)
				System.out.println("All salary values added to the database payroll_details");
			else
				System.out.println("Data not entered into the payroll_details");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		readPayrollDetailDB();
	}

	private void readPayrollDetailDB() throws DatabaseException {
		String query = "select * from payroll_detail;";
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String emp_name = resultSet.getString("Employee_name");
				double deduction = resultSet.getDouble("deduction");
				double taxable_salary = resultSet.getDouble("taxable_salary");
				double tax = resultSet.getDouble("tax");
				double net_pay = resultSet.getDouble("net_pay");
				System.out.println("Employee Name: " + emp_name + " Deduction: Rs. " + deduction
						+ " Taxablesalary: Rs. " + taxable_salary + " Tax: Rs. " + tax + " Net Pay: Rs. " + net_pay);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addEmployeeDataUsingThread(List<EmployeePayrollData> employeeList) {
		Map<Integer, Boolean> employeeInsertionStatus = new HashMap<Integer, Boolean>();
		employeeList.forEach(EmployeePayrollData -> {
			Runnable task = () -> {
				employeeInsertionStatus.put(EmployeePayrollData.hashCode(), false);
				try {
					System.out.println("Employee Being added: " + Thread.currentThread().getName());
					addEmployeeToPayroll(EmployeePayrollData.getEmpName(), EmployeePayrollData.getGender(),
							EmployeePayrollData.getSalary(), EmployeePayrollData.getStart());
					employeeInsertionStatus.put(EmployeePayrollData.hashCode(), true);
					System.out.println("Employee added: " + Thread.currentThread().getName());
				} catch (DatabaseException e) {
					System.out.println(e.getMessage());
				}
			};
			Thread thread = new Thread(task, EmployeePayrollData.getEmpName());
			thread.start();
		});
		while (employeeInsertionStatus.containsValue(false)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

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
