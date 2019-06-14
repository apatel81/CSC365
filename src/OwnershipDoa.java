import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class OwnershipDoa implements objDoa<Ownership>{
   private Connection connection;

   public OwnershipDoa(Connection connection){
      this.connection = connection;
   }


   public Ownership getByKey(){

      return null;
   }

   public Set<Ownership> getAll(){
      return null;
   }

   public Boolean insert(Ownership o){
      Boolean successful = false;
      PreparedStatement ps = null;


      try {
         ps = this.connection.prepareStatement(
                 "INSERT INTO Ownership (cId,ccn) VALUES (?, ?);");
         ps.setLong(1, o.getcId());
         ps.setLong(2, o.getCcn());
         successful = ps.execute();
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

   public Boolean update(Ownership o){
      return false;
   }

   public Boolean delete(Ownership o){
      return false;
   }


}
