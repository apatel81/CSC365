import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.io.IOException;
import java.sql.SQLException;

public class CreditCardDoa implements objDoa<CreditCard>{

   private Connection connection;

   public CreditCardDoa(Connection connection){
      this.connection = connection;
   }

   public CreditCard getByKey(){

      return null;
   }

   public Set<CreditCard>getAll(){
      return null;
   }

   public Boolean insert(CreditCard cc){
      Boolean successful = false;
      PreparedStatement ps = null;



      try {
         ps = this.connection.prepareStatement(
                 "INSERT INTO Credit_Card (ccn, type, secCode, expDate, bill_address) VALUES (?, ?, ?, ?, ?);");
         ps.setLong(1, cc.getCcn());
         ps.setString(2, cc.getType());
         ps.setInt(3, cc.getSecCode());
         ps.setString(4, cc.getExpDate());
         ps.setString(5, cc.getBill_adress());
         successful = ps.execute();

         //System.out.println(successful + "fffff");
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            ps.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return successful;
   }

   public Boolean update(CreditCard cc){
      return false;
   }

   public Boolean delete(CreditCard cc){
      return false;
   }


   public int checkCreditCardValid(String name, long phone, Long ccn, int secCode, String expDate){
      Set<CreditCard> CCSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      int ret = -1;

      try {
         ps = this.connection.prepareStatement("Select C.cId,C.name,C.address,C.phone FROM Customer C JOIN Ownership O\n" +
                 "ON C.cId = O.Cid\n" +
                 "JOIN Credit_Card CC ON\n" +
                 "CC.ccn = O.ccn where C.name = ? and C.phone = ? and CC.ccn = ? and CC.secCode = ? and CC.expDate = ?;");


         ps.setString(1, name);
         ps.setLong(2, phone);
         ps.setLong(3, ccn);
         ps.setInt(4,secCode);
         ps.setString(5,expDate);

         resultSet = ps.executeQuery();
         try {
            System.out.println("uuuuu");
            while(resultSet.next()) {
               System.out.println("fffff");
               ret = resultSet.getInt("cId");
            }
         }catch (Exception e){
            System.out.println("fpppp");
            ret = -1;
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            resultSet.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         try {
            ps.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }

      System.out.println("f00000p");
      return ret;
   }

   public Boolean checkCardExists(long ccn){
      Set<CreditCard> CCSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      int ret = -1;

      try {
         ps = this.connection.prepareStatement("Select * FROM Credit_Card CC " +
                 "where CC.ccn = ?;");

         ps.setLong(1, ccn);

         resultSet = ps.executeQuery();
         CCSet = unpackCreditCard(resultSet);

      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            resultSet.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         try {
            ps.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }


      try {
         CreditCard c = (CreditCard) CCSet.toArray()[0];
         //System.out.println("fdfdf");
         return true;
      }catch (Exception e){
         //System.out.println("fff");
         return false;
      }


   }

   public Set<CreditCard> getAllCreditCardsOfUser(Customer c){
      Set<CreditCard> creditCardSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      try {
         ps = this.connection.prepareStatement("Select CC.ccn,CC.type,CC.secCode,CC.expDate,CC.bill_address" +
                 " FROM Ownership O JOIN" +
                 " Credit_Card CC ON CC.ccn = O.ccn where O.cId = ?");
         ps.setInt(1, c.getcId());

         resultSet = ps.executeQuery();
         creditCardSet = unpackCreditCard(resultSet);
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            resultSet.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         try {
            ps.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      //System.out.println("getCustomerHistory");
      //System.out.println("iteration thing"+ cust3.iterator().next());
      return creditCardSet;
   }

   private Set<CreditCard> unpackCreditCard(ResultSet rs)throws SQLException{
      Set<CreditCard> ccList = new HashSet<CreditCard>();

      while(rs.next()) {
         CreditCard cc = new CreditCard(
                 rs.getLong("ccn"),
                 rs.getString("type"),
                 rs.getInt("secCode"),
                 rs.getString("expDate"),
                 rs.getString("bill_address"));
         ccList.add(cc);
      }
      //System.out.println("unpacked");
      return ccList;
   }


}
