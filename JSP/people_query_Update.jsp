<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	String peopleno = request.getParameter("no");
	String name = request.getParameter("name");
	String email = request.getParameter("email");
	String memo = request.getParameter("memo");
	String phonetel = request.getParameter("phonetel");
	String phoneno = request.getParameter("phoneno");
		
//------
	String url_mysql = "jdbc:mysql://localhost/address?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
		
		String A = "UPDATE people INNER JOIN phone ON peopleno = people_peopleno";
		String B = " SET people.peoplename = ?, people.peopleemail = ?, people.peoplememo = ?, phone.phonetel = ?";
		String C = " WHERE phoneno = ?"; 
	
	    ps = conn_mysql.prepareStatement(A+B+C);
	    ps.setString(1, name);
	    ps.setString(2, email);
		ps.setString(3, memo);
		ps.setString(4, phonetel);
		ps.setInt(5, Integer.parseInt(phoneno));    
	    ps.executeUpdate();
	
	    conn_mysql.close();
	} 
	
	catch (Exception e){
	    e.printStackTrace();
	}
	
%>

