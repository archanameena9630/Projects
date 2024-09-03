import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;


  class bankManagment{
     private static final int NULL=0;
     static Connection con = conection.getConnection();
      static String sql=" ";
      public static boolean
      createAccount (String name,int passCode){
          try{
               if(name.equals(" ")||passCode==0){
System.out.println("All feild requirend");
return false;
               }
               Statement st=con.createStatement();
               sql="insert into customer (cname,balance,pass_code)values('"+name+"',1000,"+passCode + ")"  ;  
              if(st.executeUpdate(sql)==1){
               System.out.println(name+"now you login");
               return true;
              }
          }
          catch(SQLIntegrityConstraintViolationException e){
               System.out.println("username not Available");
          }
          catch(Exception e){
               e.printStackTrace();
          }
    return false;
      }
      public static boolean
      loginAccount(String name, int passCode){
          try{
               if(name.equals("")||passCode==0){
                    System.out.println("All fied required");
                    return false;
               }
               sql="select * from customer where cname='"+name +"'and pass_code="+passCode;
               PreparedStatement st=con.prepareStatement(sql);
               ResultSet rs=st.executeQuery();
               BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));

               if(rs.next()){
                    int ch=5;
                    int amt=0;
                    int senderAc=rs.getInt("ac_no");
                    int receiveAc;
                    while(true){
                         try{
                              System.out.println("hello"+ rs.getString("cname"));
                              System.out.println("1) Transfer money");
                              System.out.println("2)view Balance");
                              System.out.println("5) logOut");

                              System.out.println("Enter choice");
                              ch=Integer.parseInt(sc.readLine());
                              if(ch==1){
                                   System.out.print("enter Receiver A/c No :");
                                   receiveAc=Integer.parseInt(sc.readLine());
                                   System.out.print("Enter Amount :");
                                   amt=Integer.parseInt(sc.readLine());

                                   if(bankManagment.transferMoney(senderAc, receiveAc,
                                   amt)){
                                        System.out.println("MSG : Money sent successfully!\n");
                                   }
                   else{
                    System.out.println("ERR : Failed!\n");
                   }
                              }
                              else if(ch==2){
                                   bankManagment.getBalance(senderAc);

                              }
                              else if(ch ==5){
                                   break;
                              }
                              else{
                                   System.out.println("Err : Enter valid input!\n");
                              }
                         }
                         catch(Exception e){
                              e.printStackTrace();
                         }
                    }
               }
          else{
               return false;
          }
          return true;
          }
          catch (SQLIntegrityConstraintViolationException e){
               System.out.println("username not Available ");
          }
          catch(Exception e){
               e.printStackTrace();
          }
          return false;
      }
      public static void 
      getBalance(int acNo){
          try{
               sql="select * from customer where ac_no=" +acNo;
               PreparedStatement st=con.prepareStatement(sql);
               ResultSet rs =st.executeQuery(sql);
               System.out.println(
                    "-------------------------------------------------");
                    System.out.printf("%12s %10s %10s\n","Account No","Name","Balence");
          
                    while (rs.next()) {
                         System.out.printf("%20d %10s %10d.00\n",
                                      rs.getInt("ac_no"),
                                      rs.getString("cname"),
                                      rs.getInt("balance"));
                    }
                    System.out.println(
                         "--------------------------------------------\n");
          }
          catch(Exception e){
               e.printStackTrace();
          }
      }
      public static boolean transferMoney(int sender_ac,
                                               int reveiver_ac,
                                               int amount)
     throws SQLException {
          if(reveiver_ac == NULL || amount == NULL){
               System.out.println("All Field Required ");
               return false;
          }
          try{
               con.setAutoCommit(false);
               sql="select * from customer where ac_no="+ sender_ac;
               PreparedStatement ps=con.prepareStatement(sql);
               ResultSet rs =ps.executeQuery(sql);

               if(rs.next()){
                    if(rs.getInt("balance")< amount){
                         System.out.println("Insufficient Balance!");
                         return false;
                    }
               }
               Statement st=con.createStatement();
               con.setSavepoint();
               sql = "update customer set balance=balance-" + amount + " where ac_no=" + sender_ac;
               if (st.executeUpdate(sql) == 1) {
                   System.out.println("Amount Debited");
               }
               
               sql = "update customer set balance=balance+" + amount + " where ac_no=" + reveiver_ac;
               st.executeUpdate(sql);
               con.commit();
               return true;
               

          }
          catch(Exception e){
               e.printStackTrace();
               con.rollback();
          }
          return false;
     }
}
  