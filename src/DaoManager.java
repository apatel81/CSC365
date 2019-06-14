//import com.sun.xml.internal.xsom.impl.scd.Step;
//import jdk.nashorn.internal.runtime.ECMAException;

import javax.security.auth.login.CredentialException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.io.File;
import java.util.Scanner;

public class DaoManager {

   private String user;
   private String pass;
   private String driver;
   private String URL;
   private String filename;
   private Connection connection;


   public DaoManager(String filename){
      this.user = null;
      this.pass = null;
      this.driver = null;
      this.URL = null;
      this.connection = null;
      this.filename = filename;
   }

   public Connection getConnection(){
      if(this.connection != null){
         return connection;
      }
      else{
         return null;
      }
   }

   public CreditCardDoa getCreditCardDoa(){
      return new CreditCardDoa(this.connection);
   }

   public RoomsDoa getRoomsDoa(){
      return new RoomsDoa(this.connection);
   }

   public CustomerDoa getCustomerDoa(){
      return new CustomerDoa(this.connection);
   }

   public OwnershipDoa getOwnershipDoa(){
      return new OwnershipDoa(this.connection);
   }

   public ReservationsDoa getReservationsDoa(){
      return new ReservationsDoa(this.connection);
   }

   public TransactionDoa getTransactionDoa(){
      return new TransactionDoa(this.connection);
   }

   private void getUserAndPass() throws IOException, SQLException, ClassNotFoundException {
      File f = new File(this.filename);
      Scanner s = new Scanner(f);
      this.user = s.nextLine();
      this.pass = s.nextLine();
      this.driver = s.nextLine();
      this.URL = s.nextLine();

      int count = 0;
      while (count < 20){

         Class.forName(this.driver);
         try {
            if (this.connection == null || this.connection.isClosed()) {
               Connection connObj = DriverManager.getConnection(this.URL, this.user, this.pass);
               this.connection = connObj;
            }
            return;
         }
         catch (Exception e){

         }

         count +=1;
      }
   }

   public int setupConnection(){
      try {
         getUserAndPass();
         return 1;
      }
      catch (Exception e){
         System.out.println("Connection setup Failed");
         return -1;
      }
   }

   public int close() {
      try
      {
         if(this.connection != null && !this.connection.isClosed())
            this.connection.close();
         return 1;
      }
      catch(SQLException e)
      {
         System.out.println("Message to server failed please try again 1");
         return -1;
      }
   }


   public int setAutoCommitFalse(){
      try {
         this.connection.setAutoCommit(false);
         return 1;
      }catch (Exception e){
         System.out.println("Message to server failed please try again 2");
         return -1;
      }
   }
   public int setAutoCommitTrue(){
      try {
         this.connection.setAutoCommit(true);
         return 1;
      }catch (Exception e){
         System.out.println("Message to server failed please try again 3");
         return -1;
       }
   }

   public int commitChanges(){
      try {
         this.connection.commit();
         return 1;
      }catch (Exception e){
        System.out.println("Message to server failed please try again 4");
        return -1;
      }
   }

    public int rollbackChanges(){
        try {
            this.connection.rollback();
            return 1;
        }catch (Exception e){
            System.out.println("Message to server failed please try again 5");
            return -1;
        }
    }


}
