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
 * 充值
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
				  response.setCharacterEncoding("UTF-8");//设置将字符以"UTF-8"编码输出到客户端浏览器
			      //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
			      response.setHeader("content-type", "text/html;charset=UTF-8");
			      System.out.println("收到的 studentnumber= "+studentnumber);
			      System.out.println("收到的 type= "+type);
				int status = -1;//状态  0-充值成功 1.充值失败
				if("chongzhi".equals(type)){
					//如果是充值请求，要先查询是否有充值记录
					if(findByNum(Integer.valueOf(studentnumber),password)) {
						//有充值 记录，修改这条数据
						//先查询出余额
						String balance = findBalance(Integer.valueOf(studentnumber),password);
						//再修改余额为之前的金额+充值的金额
						if(updateRecharge(Integer.valueOf(studentnumber),Integer.valueOf(pay)+Integer.valueOf(balance),password)==0){
							System.out.println("追加充值成功");
							status = 0;
						}else{
							System.out.println("追加充值失败");
							status = 1;
						}
					}else {
						//没有充值 记录，直接 插入一条数据
						if(recharge(Integer.valueOf(studentnumber),password,Integer.valueOf(pay))==0){
							System.out.println("充值成功");
							status = 0;
						}else{
							status = 1;
							System.out.println("充值失败");
						}
					}
					//充值后查询
//					//自定义返回消息
					response.getWriter().append("{\"status\":"+status+",\"studentnumber\":"+studentnumber+"}");
				}else if("demand".equals(type)){
					//查询余额请求
					String balances = findBalance(Integer.valueOf(studentnumber),password);
					//自定义返回消息
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
	 * 根据学号查询是否有充值 记录
	 * @param studentnumber
	 * @return
	 */
	 public static boolean findByNum(int studentnumber,String userpsd) {
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
	            String sql = "select * from balance where studentnum="+studentnumber+" AND userpsd="+userpsd;
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
	                //获取stuid这列数据
	                //输出结果
	                System.out.println(studentnum);
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
	 
	 /**
		 * 根据学号和密码查询余额
		 * @param studentnumber
		 * @return
		 */
		 public static String findBalance(int studentnumber,String userpsd) {
			 String balance = "";
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
		            String sql = "select * from balance where studentnum="+studentnumber+" AND userpsd="+userpsd;
		            //3.ResultSet类，用来存放获取的结果集！！
		            ResultSet rs = statement.executeQuery(sql);
		            System.out.println("-----------------");
		            System.out.println("执行结果如下所示:");  
		            System.out.println("-----------------");  
		            System.out.println("-学号-");  
		            System.out.println("-----------------");  
		             
		            while(rs.next()){
		                //获取stuname这列数据
		            	balance = rs.getString("userbalance");
		                //获取stuid这列数据
		                //输出结果
		                System.out.println("当前余额="+balance);
		            }
		            rs.close();
		            con.close();
		        } catch(ClassNotFoundException e) {   
		        	balance = "";
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
				return balance;
		    }
	
	 /**
	  * 直接充值 
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
            // 获取连接
            conn = JDBCUtils.getConnection();
            
            // 编写sql
            String sql = "insert into balance values (?,?,?)";
            
            // 创建语句执行者
            st= (PreparedStatement) conn.prepareStatement(sql);
            
            //设置参数
            st.setInt(1, studentnumber);
            st.setString(2,userpsd);
            st.setInt(3, pay);
            
            // 执行sql
            int i = st.executeUpdate();
            
            if(i==1) {
            	isRecharge = 0;
                System.out.println("数据添加成功！");
                
            }else {
            	isRecharge = 1;
                System.out.println("数据添加失败！");
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
	  * 修改充值余额 
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
           // 获取连接
           conn = JDBCUtils.getConnection();
           
           // 编写sql
           String sql = "UPDATE balance SET userbalance="+pay+" WHERE studentnum="+studentnumber+" AND userpsd="+password;
           System.out.println("sql="+sql);
           // 创建语句执行者
           st= (PreparedStatement) conn.prepareStatement(sql);
           
           //设置参数
//           st.setInt(1, studentnumber);
//           st.setInt(2, pay);
           
           // 执行sql
           int i = st.executeUpdate();
           
           if(i==1) {
           	isRecharge = 0;
               System.out.println("数据添加成功！");
               
           }else {
           	isRecharge = 1;
               System.out.println("数据添加失败！");
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
