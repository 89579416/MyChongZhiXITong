package com.s;

import java.io.IOException;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  ע��
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		 request.setCharacterEncoding("utf-8");
		String studentnumber = request.getParameter("studentnumber");
		String password = (String)request.getParameter("password");
		String school = (String) request.getParameter("school");
		school.getBytes("iso8859-1"); 
		school =  new String(school.getBytes("iso8859-1"), "UTF-8");
		  response.setCharacterEncoding("UTF-8");//���ý��ַ���"UTF-8"����������ͻ��������
	      //ͨ��������Ӧͷ�����������UTF-8�ı�����ʾ���ݣ����������仰����ô�������ʾ�Ľ�������
	      response.setHeader("content-type", "text/html;charset=UTF-8");
		System.out.println("�յ��� studentnumber= "+studentnumber);
		System.out.println("�յ��� password= "+password);
		System.out.println("�յ��� school="+school);
		int status = 0;//״̬  0-δע�� 1.��ע��  2��ע��ɹ� 3��ע��ʧ��
		if(findByNum(Integer.valueOf(studentnumber))==false) {
			boolean isRegister = register(Integer.valueOf(studentnumber),password,school);
			if(isRegister) {
				status = 2;
			}else {
				status = 3;
			}
		}else {
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
	 *  ע��
	 * @param userNumber
	 * @param password
	 * @return
	 */
	public static boolean register(int userNumber,String password,String school) {
		boolean isRigister = false;//�Ƿ�ע��ɹ�
		Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // ��ȡ����
            conn = JDBCUtils.getConnection();
            
            // ��дsql
            String sql = "insert into myuser values (?,?,?)";
            
            // �������ִ����
            st= (PreparedStatement) conn.prepareStatement(sql);
            
            //���ò���
            st.setInt(1, userNumber);
            st.setString(2, password);
            st.setString(3, school);
            
            // ִ��sql
            int i = st.executeUpdate();
            
            if(i==1) {
            	isRigister = true;
                System.out.println("������ӳɹ���");
                
            }else {
            	isRigister = false;
                System.out.println("�������ʧ�ܣ�");
            }
            
        } catch (SQLException e) {
        	isRigister = false;
            e.printStackTrace();
        }finally {
//        	�ͷ���Դ
            JDBCUtils.colseResource(conn, st, rs);
        }
        return isRigister;
        
	}
	
	/**
	 * ����ѧ�Ų�ѯѧ���Ƿ���� 
	 * @param studentnumber
	 * @return
	 */
	 public static boolean findByNum(int studentnumber) {
		 boolean isRegister = false;
	        //����Connection����
	        Connection con;
	        //����������
	        String driver = "com.mysql.jdbc.Driver";
	        //URLָ��Ҫ���ʵ����ݿ���mydata
	        String url = "jdbc:mysql://localhost:3306/mysql";
	        //MySQL����ʱ���û���
	        String user = "root";
	        //MySQL����ʱ������
	        String password = "123456";
	        //������ѯ�����
	        try {
	            //������������
	            Class.forName(driver);
	            //1.getConnection()����������MySQL���ݿ⣡��
	            con = (Connection) DriverManager.getConnection(url,user,password);
	            if(!con.isClosed())
	                System.out.println("Succeeded connecting to the Database!");
	            //2.����statement���������ִ��SQL��䣡��
	            Statement statement = (Statement) con.createStatement();
	            //Ҫִ�е�SQL���
	            String sql = "select * from myuser where studentnum="+studentnumber;
	            //3.ResultSet�࣬������Ż�ȡ�Ľ��������
	            ResultSet rs = statement.executeQuery(sql);
	            System.out.println("-----------------");
	            System.out.println("ִ�н��������ʾ:");  
	            System.out.println("-----------------");  
	            System.out.println("-ѧ��-");  
	            System.out.println("-----------------");  
	             
	            String studentnum = null;
	            while(rs.next()){
	            	isRegister = true;
	                //��ȡstuname��������
	            	studentnum = rs.getString("studentnum");
	            	String userlocated = rs.getString("userlocated");
	                //��ȡstuid��������
	                //������
	                System.out.println("studentnum="+studentnum);
	                System.out.println("userlocated="+userlocated);
	            }
	            rs.close();
	            con.close();
	        } catch(ClassNotFoundException e) {   
	        	isRegister = false;
	            //���ݿ��������쳣����
	            System.out.println("Sorry,can`t find the Driver!���ݿ��������쳣����");   
	            e.printStackTrace();   
	            } catch(SQLException e) {
	            //���ݿ�����ʧ���쳣����
	            e.printStackTrace(); 
	            }catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }finally{
	            System.out.println("���ݿ����ݳɹ���ȡ����");
	        }
			return isRegister;
	    }

}
