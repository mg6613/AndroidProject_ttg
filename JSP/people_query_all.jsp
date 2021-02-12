<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    String useremail = request.getParameter("email");

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

        String WhereDefault = "select peopleno, peoplename, (SELECT JSON_ARRAYAGG(phonetel) FROM phone group by people_peopleno having people_peopleno = peopleno) peoplephone, peopleemail, peoplerelation, peoplememo, peopleimage, ";
        String WhereDefault1 = "s.peoplefavorite favorite, s.peopleemg emergency, r.userinfo_useremail useremail from people peo, phone ph, statuspeople s, register r ";
        String WhereDefault2 = "where peo.peopleno = ph.people_peopleno and s.people_peopleno = peo.peopleno and r.people_peopleno = peo.peopleno ";
        String WhereDefault3 = "and r.userinfo_useremail = ? group by peo.peopleno, s.peoplefavorite, s.peopleemg, r.userinfo_useremail order by peoplename";

        ps = conn_mysql.prepareStatement(WhereDefault + WhereDefault1 + WhereDefault2 + WhereDefault3); // 
        ps.setString(1, useremail);
        rs = ps.executeQuery();

%>
		{ 
  			"people_info"  : [ 
<%
        while (rs.next()) {
            if (count == 0) {

            }else{
%>
            , 
<%
            }
%>            
			{
			        "no" : "<%=rs.getString(1) %>", 
			        "name" : "<%=rs.getString(2) %>",   
			        "tel" : <%=rs.getString(3) %>,  
              		"email" : "<%=rs.getString(4) %>",
            		"relation" : "<%=rs.getString(5) %>",
           	    	"memo" : "<%=rs.getString(6) %>",
            		"image" : "<%=rs.getString(7) %>",
            		"favorite" : "<%=rs.getString(8) %>",
            		"emergency" : "<%=rs.getString(9) %>",
            		"useremail" : "<%=rs.getString(10) %>"
			}

<%		
        count++;
        }
%>
		  ] 
		} 
<%		
        conn_mysql.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
	
%>
