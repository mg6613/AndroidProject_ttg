<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	int result = 0;
	String peoplefavorite = request.getParameter("peoplefavorite");
	String peopleno = request.getParameter("peopleno");
		
//------
	String url_mysql = "jdbc:mysql://localhost/address?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
		Statement stmt_mysql = conn_mysql.createStatement();
		
		String A = "UPDATE statuspeople SET peoplefavorite = ? WHERE people_peopleno = ?";

		ps = conn_mysql.prepareStatement(A);
		ps.setInt(1, Integer.parseInt(peoplefavorite));
		ps.setInt(2, Integer.parseInt(peopleno));
		ps.executeUpdate();			
	    conn_mysql.close();
	} 
	
	catch (Exception e){
	    e.printStackTrace();
	}

	
%>

