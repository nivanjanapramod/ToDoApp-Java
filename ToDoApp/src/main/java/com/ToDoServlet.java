package com;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.todo.apps.Todo;


public class ToDoServlet extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	

	public String execute() throws ServletException, IOException, JSONException {
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		String action = request.getServletPath();
		switch(action) {
		case "/task/insert" : doPut(request,response);
						break;
		case "/task/update" : doPost(request,response);
						break;
		case "/task/delete" : doDelete(request,response);
						break;
		case "/task/get" : doGet(request,response);
					break;
		case "/task/getEditForm" : selectTodoById(request, response);
									break;
		}
		return null;
	}
	
    public ToDoServlet() {
        super();    
    }

	
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false","root","Niva@2000");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try(
		
			Connection connection = getConnection()){
			Cookie[] cookies = request.getCookies();
	        int value = Integer.valueOf(cookies[1].getValue());
	        System.out.println(value);
			PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where userid=?;");
			preparedStatement.setInt(1, value);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			JSONArray ja = new JSONArray();
			while (rs.next()) {
				JSONObject j = new JSONObject();
				j.put("id", rs.getInt("id"));
				j.put("title", rs.getString("title"));
				j.put("description", rs.getString("description"));
				j.put("completed", rs.getBoolean("completed"));
				ja.put(j);
			}
			request.setAttribute("list", ja);
			System.out.println(ja);
			response.setContentType("application/json");  
			PrintWriter out = response.getWriter();
			out.print(ja);
			out.flush();
		}
		catch(SQLException e) {
			printSQLException(e);
		}
	}
	
	protected void selectTodoById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try(
			Connection connection = getConnection()){
			PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where id=?;");
			int id = Integer.valueOf(request.getParameter("id"));
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			JSONObject j = new JSONObject();
			while (rs.next()) {
				j.put("id", rs.getInt("id"));
				j.put("title", rs.getString("title"));
				j.put("description", rs.getString("description"));
				j.put("completed", rs.getInt("completed"));
			}
			response.setContentType("application/json");  
			PrintWriter out = response.getWriter();
			out.print(j);
			out.flush();
		}
		catch(SQLException e) {
			printSQLException(e);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try(Connection connection = getConnection()){
			PreparedStatement preparedStatement = connection.prepareStatement("update todo set title = ?,description= ?, completed =? where id = ?;");
			String description = (String) request.getParameter("description");
			String title = (String) request.getParameter("title");
			int id = Integer.valueOf(request.getParameter("id"));
			int completed =Integer.valueOf(request.getParameter("completed"));
			int userid = Integer.valueOf(request.getParameter("userid"));
			Todo todo = new Todo(id, title, description, completed, userid);
			preparedStatement.setInt(4, id);
			preparedStatement.setString(1, todo.getTitle());
			preparedStatement.setString(2, todo.getDescription());
			preparedStatement.setInt(3, todo.isCompleted());
			preparedStatement.executeUpdate();
			connection.commit();
			preparedStatement.close();
			connection.close();

		}
		catch(SQLException e) {
			printSQLException(e);
		}
		//doGet(request, response);
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try(Connection connection = getConnection()){
			String description = (String) request.getParameter("description");
			String title = (String) request.getParameter("title");
			int completed =Integer.valueOf(request.getParameter("completed"));
			int userid = Integer.valueOf(request.getParameter("userid"));
			Todo todo = new Todo(title, description, completed, userid);
			PreparedStatement preparedStatement = connection.prepareStatement("insert into todo (title,description,completed,userid) values(?,?,?,?);");
			System.out.println(preparedStatement);
			//ResultSet rs = preparedStatement.executeQuery();
			preparedStatement.setString(1, todo.getTitle());
			preparedStatement.setString(2, todo.getDescription());
			preparedStatement.setInt(3, todo.isCompleted());
			preparedStatement.setInt(4, todo.getUserid());
			preparedStatement.executeUpdate();
			connection.commit();
			preparedStatement.close();
			connection.close();
		}
		catch(SQLException e) {
			printSQLException(e);
		}
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try(Connection connection = getConnection()){
			PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id=?");
			int id = Integer.valueOf(request.getParameter("id"));
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
			//ResultSet rs = preparedStatement.executeQuery();
			preparedStatement.setInt(1, request.getContentLength());
		}
		catch(SQLException e) {
			printSQLException(e);
		}
	}

}
