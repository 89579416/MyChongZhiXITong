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
 * ��ֵ
 * Servlet implementation class RechargeServlet
 */
@WebServlet("/RechargeServlet")
public class RechargeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RechargeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		 request.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				String studentnumber = (String)request.getParameter("studentnumber");
				String password = (String )request.getParameter("password");
				String pay = (String)request.getParameter("pay");
				String type = (String)request.getParameter("type");
				  response.setCharacterEncoding("UTF-8");//���ý��ַ���"UTF-8"����������ͻ��������
			      //ͨ��������Ӧͷ�����������UTF-8�ı�����ʾ���ݣ����������仰����ô�������ʾ�Ľ�������
			      response.setHeader("content-type", "text/html;charset=UTF-8");
			      System.out.println("�յ��� studentnumber= "+studentnumber);
			      System.out.println("�յ��� type= "+type);
				int status = -1;//״̬  0-��ֵ�ɹ� 1.��ֵʧ��
				if("chongzhi".equals(type)){
					//����ǳ�ֵ����Ҫ�Ȳ�ѯ�Ƿ��г�ֵ��¼
					if(findByNum(Integer.valueOf(studentnumber),password)) {
						//�г�ֵ ��¼���޸���������
						//�Ȳ�ѯ�����
						String balance = findBalance(Integer.valueOf(studentnumber),password);
						//���޸����Ϊ֮ǰ�Ľ��+��ֵ�Ľ��
						if(updateRecharge(Integer.valueOf(studentnumber),Integer.valueOf(pay)+Integer.valueOf(balance),password)==0){
							System.out.println("׷�ӳ�ֵ�ɹ�");
							status = 0;
						}else{
							System.out.println("׷�ӳ�ֵʧ��");
							status = 1;
						}
					}else {
						//û�г�ֵ ��¼��ֱ�� ����һ������
						if(recharge(Integer.valueOf(studentnumber),password,Integer.valueOf(pay))==0){
							System.out.println("��ֵ�ɹ�");
							status = 0;
						}else{
							status = 1;
							System.out.println("��ֵʧ��");
						}
					}
					//��ֵ���ѯ
//					//�Զ��巵����Ϣ
					response.getWriter().append("{\"status\":"+status+",\"studentnumber\":"+studentnumber+"}");
				}else if("demand".equals(type)){
					//��ѯ�������
					String balances = findBalance(Integer.valueOf(studentnumber),password);
					//�Զ��巵����Ϣ
					response.getWriter().append("{\"balances\":"+balances+",\"studentnumber\":"+studentnumber+"}");
				}
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * ����ѧ�Ų�ѯ�Ƿ��г�ֵ ��¼
	 * @param studentnumber
	 * @return
	 */
	 public static boolean findByNum(int studentnumber,String userpsd) {
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
	            String sql = "select * from balance where studentnum="+studentnumber+" AND userpsd="+userpsd;
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
	                //��ȡstuid��������
	                //������
	                System.out.println(studentnum);
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
	 
	 /**
		 * ����ѧ�ź������ѯ���
		 * @param studentnumber
		 * @return
		 */
		 public static String findBalance(int studentnumber,String userpsd) {
			 String balance = "";
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
		            String sql = "select * from balance where studentnum="+studentnumber+" AND userpsd="+userpsd;
		            //3.ResultSet�࣬������Ż�ȡ�Ľ��������
		            ResultSet rs = statement.executeQuery(sql);
		            System.out.println("-----------------");
		            System.out.println("ִ�н��������ʾ:");  
		            System.out.println("-----------------");  
		            System.out.println("-ѧ��-");  
		            System.out.println("-----------------");  
		             
		            while(rs.next()){
		                //��ȡstuname��������
		            	balance = rs.getString("userbalance");
		                //��ȡstuid��������
		                //������
		                System.out.println("��ǰ���="+balance);
		            }
		            rs.close();
		            con.close();
		        } catch(ClassNotFoundException e) {   
		        	balance = "";
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
				return balance;
		    }
	
	 /**
	  * ֱ�ӳ�ֵ 
	  * @param studentnumber
	  * @param pay
	  * @return
	  */
	public int recharge(int studentnumber,String userpsd,int pay){
		int isRecharge = 1;
		Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // ��ȡ����
            conn = JDBCUtils.getConnection();
            
            // ��дsql
            String sql = "insert into balance values (?,?,?)";
            
            // �������ִ����
            st= (PreparedStatement) conn.prepareStatement(sql);
            
            //���ò���
            st.setInt(1, studentnumber);
            st.setString(2,userpsd);
            st.setInt(3, pay);
            
            // ִ��sql
            int i = st.executeUpdate();
            
            if(i==1) {
            	isRecharge = 0;
                System.out.println("������ӳɹ���");
                
            }else {
            	isRecharge = 1;
                System.out.println("�������ʧ�ܣ�");
            }
            
        } catch (SQLException e) {
        	isRecharge = 1;
            e.printStackTrace();
        }finally {
            JDBCUtils.colseResource(conn, st, rs);
        }
		
		return 0;
	}
	
	/**
	  * �޸ĳ�ֵ��� 
	  * @param studentnumber
	  * @param pay
	  * @return
	  */
	public int updateRecharge(int studentnumber,int pay,String password){
		int isRecharge = 1;
		Connection conn = null;
       PreparedStatement st = null;
       ResultSet rs = null;
       try {
           // ��ȡ����
           conn = JDBCUtils.getConnection();
           
           // ��дsql
           String sql = "UPDATE balance SET userbalance="+pay+" WHERE studentnum="+studentnumber+" AND userpsd="+password;
           System.out.println("sql="+sql);
           // �������ִ����
           st= (PreparedStatement) conn.prepareStatement(sql);
           
           //���ò���
//           st.setInt(1, studentnumber);
//           st.setInt(2, pay);
           
           // ִ��sql
           int i = st.executeUpdate();
           
           if(i==1) {
           	isRecharge = 0;
               System.out.println("������ӳɹ���");
               
           }else {
           	isRecharge = 1;
               System.out.println("�������ʧ�ܣ�");
           }
           
       } catch (SQLException e) {
       	isRecharge = 1;
           e.printStackTrace();
       }finally {
           JDBCUtils.colseResource(conn, st, rs);
       }
		
		return 	isRecharge;
	}

}
