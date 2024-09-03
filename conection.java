import java.sql.Connection;
import java.sql.DriverManager;

 public class conection{
    static Connection con=null;
    public static Connection getConnection(){
try{
    String mysqlJDBCDriver="com.mysql.cj.jdbc.Driver";
      String url="jdbc:Mysql://localhost:3306/bank";
     String usrname="root";
    String password="1234";

  Class.forName(mysqlJDBCDriver);
               con=DriverManager.getConnection(url, usrname, password);
         }
         catch(Exception e){
              System.out.println("connection faild");
         }
         return con;
    }
    
    }

