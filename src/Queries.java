import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Queries {

   //JDBC DRIVER NAME & DATABASE URL
   static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   static final String JDBC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec05group02";

   //JDBC DATABASE CREDENTIALS
   static final String JDBC_USER = "apatel81";
   static final String JDBC_PASS = "014299476";


   public void viewRooms(){
      try
      {


         //CREATES CONNECTION TO DB
         Connection connObj = DriverManager.getConnection(JDBC_DB_URL, JDBC_USER, JDBC_PASS);

         //CREATE A SQL STATEMENT
         PreparedStatement preparedStatement = connObj.prepareStatement(
                 "SELECT * FROM Rooms");

         //preparedStatement.setString()  -->  if we want to plug in certain where statement

         //EXECUTE SQL STATEMENT
         ResultSet resultObj = preparedStatement.executeQuery();
         while (resultObj.next())
         {
            System.out.println
                    (
                            "roomId: " + resultObj.getInt("roomId") + ", Room Name: " + resultObj.getString("roomName") +
                                    ", Bed Type: " + resultObj.getString("bedType") + ", Number of Beds: " +
                                    resultObj.getInt("numBeds") + ", Max Occupancy: " + resultObj.getInt("maxOcc") +
                                    ", Base Price: " + resultObj.getFloat("basePrice") + ", Room Decor: " +
                                    resultObj.getString("decor")
                    );
         }

         connObj.close();

      }
      catch (Exception sqlException)
      {
         sqlException.printStackTrace();
      }
   }

















}
