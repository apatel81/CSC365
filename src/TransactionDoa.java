import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeMap;

public class TransactionDoa implements objDoa<Transaction>{

   private Connection connection;

   public TransactionDoa(Connection connection){
      this.connection = connection;
   }

   public Transaction getByKey(){

      return null;
   }

   public Set<Transaction> getAll(){
      return null;
   }

   public Boolean insert(Transaction t){
      PreparedStatement ps = null;
      Boolean result = false;

      try
      {
         ps = this.connection.prepareStatement("INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) " +
                         "VALUES (?,?,?,?,?);");
         ps.setInt(1, t.gettId());
         ps.setLong(2, t.getCcn());
         ps.setInt(3, t.getResId());
         ps.setFloat(4, t.getAmountPaid());
         ps.setString(5, t.getDatePaid());
         result = ps.execute();

      }

      catch (SQLException e)
      {
         result = true;
         e.printStackTrace();
      }



      try
      {
         ps.close();
      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }


      return result;

   }

   public Boolean update(Transaction t){
      return false;
   }

   public Boolean delete(Transaction t){
      return false;
   }


}
