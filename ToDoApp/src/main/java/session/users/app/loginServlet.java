package session.users.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

public class loginServlet extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int userid;
	
    public loginServlet() {
        super();
        
    }
    
    public String execute() throws ServletException, IOException, JSONException, SQLException {
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		String action = request.getServletPath();
		switch(action) {
			case "/login": doPost(request,response);
							break;
			case "/logout": doGet(request,response);
							break;
		}
		return null;
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String user = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        try(Connection connection = getConnection()){
			PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username=?;");
			preparedStatement.setString(1, user);
			ResultSet rs = preparedStatement.executeQuery();
			String u = "tryname";
			String p = "password";
			while (rs.next()) {
			u = rs.getString("username");
			p = rs.getString("password");
			userid = rs.getInt("id");
			}
			if (password.equals(p) && user.equals(u)) {
		        session.setAttribute("user", user);
		        session.setMaxInactiveInterval(300);
		        System.out.println(request.getSession(false));
		        PreparedStatement pr = connection.prepareStatement("insert into session(userid, session) values(?,?);");
		        pr.setInt(1, userid);
		        String s = "";
		        s += request.getSession(false);
		        pr.setString(2, s);
		        pr.executeUpdate();
		        response.setContentType("application/json");
				JSONObject j = new JSONObject();
				j.put("userid", userid);
				out.print(j);
				out.flush();
		    }
			/*
		    else {
		    	response.setContentType("text/html");
		        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
		        out.println("Password is wrong.");
		        rd.include(request, response);
		    }*/
		    out.close();
        }
        catch(SQLException e) {
			printSQLException(e);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        System.out.println("Session before invalidate: "+ request.getSession(false));
        HttpSession session = request.getSession();
        session.invalidate();
        System.out.println("Session after invalidate: "+ request.getSession(false));
        Cookie[] cookies = request.getCookies();
        String value = cookies[1].getValue();
        System.out.println(value);
        try(Connection connection = getConnection()){
        	PreparedStatement ps = connection.prepareStatement("delete from session where userid=?;");
        	ps.setInt(1, Integer.valueOf(value));
        	ps.executeUpdate();
        }
        catch(SQLException e) {
			printSQLException(e);
		}
        for (int i = 0; i < cookies.length; i++) {
        	cookies[i].setMaxAge(0);
        }
        
	}

}
