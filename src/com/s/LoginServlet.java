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
 *  ��½
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
		response.setCharacterEncoding("UTF-8");//���ý��ַ���"UTF-8"����������ͻ��������
	    //ͨ��������Ӧͷ�����������UTF-8�ı�����ʾ���ݣ����������仰����ô�������ʾ�Ľ�������
	    response.setHeader("content-type", "text/html;charset=UTF-8");
	    System.out.println("studentnumber="+studentnumber);
	    System.out.println("password="+password);
		int status = 0;//״̬  0��½�ɹ�  1����½ʧ��
		 System.out.println("��½��ʼ��");
		if(login(Integer.valueOf(studentnumber),password)) {
			status = 0;
		}else{
			status = 1;
		}
		//�Զ��巵����Ϣ
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
		 System.out.println("�����½������");
		boolean isRigister = false;//�Ƿ�ע��ɹ�
		Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // ��ȡ����
            conn = JDBCUtils.getConnection();
            
            // ��дsql
            String sql = "select * from myuser where studentnum="+userNumber+" and userpsd="+password+"";
            System.out.println("sql="+sql);
            
            // �������ִ����
            st= (PreparedStatement) conn.prepareStatement(sql);
            
            //���ò���
//            st.setString(1, userNumber);
//            st.setString(2, password);
            
            // ִ��sql
            ResultSet i = st.executeQuery(sql);
            
            if(i.next()) {
            	isRigister = true;
                System.out.println("��½�ɹ���");
            }else {
            	isRigister = false;
                System.out.println("��½ʧ�ܣ�");
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
