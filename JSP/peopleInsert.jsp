<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	String peoplename = request.getParameter("peoplename");
	String peopleemail = request.getParameter("peopleemail");
	String peoplerelation = request.getParameter("peoplerelation");
    String peoplememo = request.getParameter("peoplememo");	
    String peopleimage = request.getParameter("peopleimage");	
		
//------
	String url_mysql = "jdbc:mysql://localhost/address?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	int result = 0; // 입력 확인 

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
		Statement stmt_mysql = conn_mysql.createStatement();
	
	    String A = "insert into address.people (peoplename, peopleemail, peoplerelation, peoplememo, peopleimage";
	    String B = ") values (?,?,?,?,?)";
	
	    ps = conn_mysql.prepareStatement(A+B);
	    ps.setString(1, peoplename);
	    ps.setString(2, peopleemail);
	    ps.setString(3, peoplerelation);
        ps.setString(4, peoplememo);
        ps.setString(5, peopleimage);
		
		result = ps.executeUpdate();
%>
		{
			"result" : "<%=result%>"
		}

<%		
	    conn_mysql.close();
	} 
	catch (Exception e){
%>
		{
			"result" : "<%=result%>"
		}
<%		
	    e.printStackTrace();
	} 
	
%>