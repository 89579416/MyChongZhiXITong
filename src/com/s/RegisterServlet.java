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
 *  注册
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
		  response.setCharacterEncoding("UTF-8");//设置将字符以"UTF-8"编码输出到客户端浏览器
	      //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
	      response.setHeader("content-type", "text/html;charset=UTF-8");
		System.out.println("收到的 studentnumber= "+studentnumber);
		System.out.println("收到的 password= "+password);
		System.out.println("收到的 school="+school);
		int status = 0;//状态  0-未注册 1.己注册  2，注册成功 3，注册失败
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
	 *  注册
	 * @param userNumber
	 * @param password
	 * @return
	 */
	public static boolean register(int userNumber,String password,String school) {
		boolean isRigister = false;//是否注册成功
		Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // 获取连接
            conn = JDBCUtils.getConnection();
            
            // 编写sql
            String sql = "insert into myuser values (?,?,?)";
            
            // 创建语句执行者
            st= (PreparedStatement) conn.prepareStatement(sql);
            
            //设置参数
            st.setInt(1, userNumber);
            st.setString(2, password);
            st.setString(3, school);
            
            // 执行sql
            int i = st.executeUpdate();
            
            if(i==1) {
            	isRigister = true;
                System.out.println("数据添加成功！");
                
            }else {
            	isRigister = false;
                System.out.println("数据添加失败！");
            }
            
        } catch (SQLException e) {
        	isRigister = false;
            e.printStackTrace();
        }finally {
//        	释放资源
            JDBCUtils.colseResource(conn, st, rs);
        }
        return isRigister;
        
	}
	
	/**
	 * 根据学号查询学生是否存在 
	 * @param studentnumber
	 * @return
	 */
	 public static boolean findByNum(int studentnumber) {
		 boolean isRegister = false;
	        //声明Connection对象
	        Connection con;
	        //驱动程序名
	        String driver = "com.mysql.jdbc.Driver";
	        //URL指向要访问的数据库名mydata
	        String url = "jdbc:mysql://localhost:3306/mysql";
	        //MySQL配置时的用户名
	        String user = "root";
	        //MySQL配置时的密码
	        String password = "123456";
	        //遍历查询结果集
	        try {
	            //加载驱动程序
	            Class.forName(driver);
	            //1.getConnection()方法，连接MySQL数据库！！
	            con = (Connection) DriverManager.getConnection(url,user,password);
	            if(!con.isClosed())
	                System.out.println("Succeeded connecting to the Database!");
	            //2.创建statement类对象，用来执行SQL语句！！
	            Statement statement = (Statement) con.createStatement();
	            //要执行的SQL语句
	            String sql = "select * from myuser where studentnum="+studentnumber;
	            //3.ResultSet类，用来存放获取的结果集！！
	            ResultSet rs = statement.executeQuery(sql);
	            System.out.println("-----------------");
	            System.out.println("执行结果如下所示:");  
	            System.out.println("-----------------");  
	            System.out.println("-学号-");  
	            System.out.println("-----------------");  
	             
	            String studentnum = null;
	            while(rs.next()){
	            	isRegister = true;
	                //获取stuname这列数据
	            	studentnum = rs.getString("studentnum");
	            	String userlocated = rs.getString("userlocated");
	                //获取stuid这列数据
	                //输出结果
	                System.out.println("studentnum="+studentnum);
	                System.out.println("userlocated="+userlocated);
	            }
	            rs.close();
	            con.close();
	        } catch(ClassNotFoundException e) {   
	        	isRegister = false;
	            //数据库驱动类异常处理
	            System.out.println("Sorry,can`t find the Driver!数据库驱动类异常处理");   
	            e.printStackTrace();   
	            } catch(SQLException e) {
	            //数据库连接失败异常处理
	            e.printStackTrace(); 
	            }catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }finally{
	            System.out.println("数据库数据成功获取！！");
	        }
			return isRegister;
	    }

}
