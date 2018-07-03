package com.s;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;

/**
 *  登陆
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 request.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String studentnumber = (String) request.getParameter("studentnumber");
		String password = (String)request.getParameter("password");
		response.setCharacterEncoding("UTF-8");//设置将字符以"UTF-8"编码输出到客户端浏览器
	    //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
	    response.setHeader("content-type", "text/html;charset=UTF-8");
	    System.out.println("studentnumber="+studentnumber);
	    System.out.println("password="+password);
		int status = 0;//状态  0登陆成功  1。登陆失败
		 System.out.println("登陆开始！");
		if(login(Integer.valueOf(studentnumber),password)) {
			status = 0;
		}else{
			status = 1;
		}
		//自定义返回消息
		response.getWriter().append("{\"status\":"+status+",\"message\":\"success\",\"studentnumber\":"+studentnumber+"}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 *  
	 * @param userNumber
	 * @param password
	 * @return
	 */
	public static boolean login(int userNumber,String password) {
		 System.out.println("进入登陆方法！");
		boolean isRigister = false;//是否注册成功
		Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // 获取连接
            conn = JDBCUtils.getConnection();
            
            // 编写sql
            String sql = "select * from myuser where studentnum="+userNumber+" and userpsd="+password+"";
            System.out.println("sql="+sql);
            
            // 创建语句执行者
            st= (PreparedStatement) conn.prepareStatement(sql);
            
            //设置参数
//            st.setString(1, userNumber);
//            st.setString(2, password);
            
            // 执行sql
            ResultSet i = st.executeQuery(sql);
            
            if(i.next()) {
            	isRigister = true;
                System.out.println("登陆成功！");
            }else {
            	isRigister = false;
                System.out.println("登陆失败！");
            }
            
        } catch (SQLException e) {
        	isRigister = false;
            e.printStackTrace();
        }finally {
            JDBCUtils.colseResource(conn, st, rs);
        }
        return isRigister;
        
	}
	
}
