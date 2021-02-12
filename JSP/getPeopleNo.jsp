<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
request.setCharacterEncoding("utf-8");
	String url_mysql = "jdbc:mysql://localhost/address?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    
    int count = 0;
    PreparedStatement ps = null;
    ResultSet rs =null;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();
        String WhereDefault = "select max(peopleno) peopleno from people";
        ps = conn_mysql.prepareStatement(WhereDefault);
        rs = ps.executeQuery();
        while(rs.next()){
        %>
		
			{
                "result" : "<%=rs.getString(1) %>"
			}

<%		
    }
        conn_mysql.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
	
%>