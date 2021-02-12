<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	int result = 0;
	int no = request.getParameter("peopleno");
	String email = request.getParameter("useremail");
		
//------
	String url_mysql = "jdbc:mysql://localhost/address?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
		Statement stmt_mysql = conn_mysql.createStatement();
		
		String A = "select peopleemg from status where userinfo_useremail = ? and people_peopleno = ?";

		ps = conn_mysql.prepareStatement(A);
		ps.setString(1, email);
		ps.setInt(2, no);
		
		resultSet = ps.executeQuery();
					
		if (resultSet.next()) {
			result = resultSet.getInt(1);
			
		}
	    conn_mysql.close();
	} 
	
	catch (Exception e){
	    e.printStackTrace();
	}
	return result;
	
%>

