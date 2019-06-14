import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ReservationsDoa implements objDoa<Reservation>{
   private Connection connection;

   public ReservationsDoa(Connection connection){
      this.connection = connection;
   }

   public Reservation getByKey(){

      return null;
   }

   public Set<Reservation> getAll(){
      Set<Reservation> reservations = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;

      try
      {
         ps = this.connection.prepareStatement("SELECT * FROM Reservations");
         resultSet = ps.executeQuery();
         reservations = unpackReservations(resultSet);
      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            resultSet.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return reservations;
   }


   private Set<Reservation> unpackReservations(ResultSet rs) throws SQLException {
      Set<Reservation> reservations = new HashSet<Reservation>();

      while (rs.next())
      {
         Reservation reservation = new Reservation(
                 rs.getInt("resId"),
                 rs.getInt("cId"),
                 rs.getInt("roomId"),
//                 rs.getString("checkIn"),
//                 rs.getString("checkOut"),
                 rs.getString("checkIn"),
                 rs.getString("checkOut"),
                 rs.getFloat("rate"),
                 rs.getInt("numAdults"),
                 rs.getInt("numKids"),
                 rs.getInt("cancelled"));

         reservations.add(reservation);
      }

      return reservations;
   }

   public Set<Reservation> searchByResId(Integer resId)
   {
      Set<Reservation> reservationSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;

      try
      {
         ps = this.connection.prepareStatement("SELECT * FROM Reservations WHERE resID = ?;");
         ps.setInt(1, resId);
         resultSet = ps.executeQuery();

         if (this.connection == null)
         {
            System.out.println("Ajay you have made some sort of mistake following Baylor's code. agggg");
         }

         reservationSet = unpackReservations(resultSet);
      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            resultSet.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return reservationSet;
   }

   public Boolean setReservationToCancelled(int resId)
   {
      Set<Reservation> reservationSet = null;
      PreparedStatement ps = null;
      Boolean result = false;

      try
      {
         ps = this.connection.prepareStatement("UPDATE Reservations SET cancelled = 1 WHERE resId = ?;");
         ps.setInt(1, resId);
         result = ps.execute();

         if (this.connection == null)
         {
            System.out.println("Ajay, query didn't update");
         }

      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            ps.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return result;
   }

   protected Boolean updateReservationDates(int resId, String checkin, String checkout)
   {
      Set <Reservation> reservationSet = null;
      PreparedStatement ps = null;
      Boolean result = false;

      try
      {
         ps = this.connection.prepareStatement("UPDATE Reservations SET checkIn = ?, checkOut = ? WHERE resId = ?;");
         ps.setString(1, checkin);
         ps.setString(2, checkout);
         ps.setInt(3, resId);
         result = ps.execute();

         if (this.connection == null)
         {
            System.out.println("Ajay, query didn't update");
         }

      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            ps.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return result;
   }

   protected boolean updateReservationRoom(int resId, int roomId)
   {
      Set <Reservation> reservationSet = null;
      PreparedStatement ps = null;
      Boolean result = false;

      try
      {
         ps = this.connection.prepareStatement("UPDATE Reservations SET roomId = ? WHERE resId = ?;");
         ps.setInt(1, roomId);
         ps.setInt(2, resId);
         result = ps.execute();

         if (this.connection == null)
         {
            System.out.println("Ajay, query didn't update");
         }

      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            ps.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return result;
   }



   protected Set<Reservation> showActiveReservations(String inputName, Long inputPhoneNum)
   {
      Set<Reservation> reservationSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;

      try
      {
         ps = this.connection.prepareStatement("SELECT * FROM Reservations WHERE cId = (SELECT cID FROM Customer WHERE name = ? AND phone = ?)" +
                 "AND cancelled = 0;");
         ps.setString(1, inputName);
         ps.setLong(2, inputPhoneNum);
         resultSet = ps.executeQuery();

         if (this.connection == null)
         {
            System.out.println("Ajay, query didn't find all reservations for Customer's ID number");
         }

         reservationSet = unpackReservations(resultSet);
      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            resultSet.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return reservationSet;
   }

   // checking availabilities with new dates
   protected Set<Reservation> checkAvailabilities(String input_checkIn, String input_checkOut, int input_roomId)
   {
      Set<Reservation> reservationSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;

      try
      {
//         ps = this.connection.prepareStatement("select * from Reservations where " +
//                 "checkIn between ? and checkOut and roomId = ? or checkOut between ? and ? and roomId = ? or " +
//                 "? between checkIn and checkOut and roomId = ? or ? between checkIn and checkOut and roomId = ?;");
         ps = this.connection.prepareStatement("select * from Reservations where " +
                 "checkOut between ? and ? and roomId = ? and cancelled = 0 or " +
                 "? between checkIn and checkOut and roomId = ? and cancelled = 0 or " +
                 "? between checkIn and checkOut and roomId = ? and cancelled = 0;");
//         ps.setString(1, input_checkIn);
//         ps.setInt(2, input_roomId);
//         ps.setString(3, input_checkIn);
//         ps.setString(4, input_checkOut);
//         ps.setInt(5, input_roomId);
//         ps.setString(6, input_checkIn);
//         ps.setInt(7, input_roomId);
//         ps.setString(8, input_checkOut);
//         ps.setInt(9, input_roomId);
         ps.setString(1, input_checkIn);
         ps.setString(2, input_checkOut);
         ps.setInt(3, input_roomId);
         ps.setString(4, input_checkIn);
         ps.setInt(5, input_roomId);
         ps.setString(6, input_checkOut);
         ps.setInt(7, input_roomId);
         resultSet = ps.executeQuery();
         if(this.connection == null)
         {
            System.out.println("aggg");
         }
         reservationSet = unpackReservations(resultSet);
      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            resultSet.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }

         try
         {
            ps.close();
         } catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return reservationSet;
   }

   public Boolean insert(Reservation r)
   {
      return false;
   }

   public Boolean update(Reservation r){
      return false;
   }

   public Boolean delete(Reservation r){
      return false;
   }

   public Boolean bookRoom(Reservation r)
   {
      Set<Reservation> reservationSet = null;
      PreparedStatement ps = null;
      Boolean result = false;

      try
      {
         ps = this.connection.prepareStatement("INSERT INTO Reservations (cId, roomId, checkIn, checkOut," +
                 "rate, numAdults, numKids) VALUES (?, ?, ?, ?, ?, ?, ?);");
         ps.setInt(1, r.getcId());
         ps.setInt(2, r.getRoomId());
         ps.setString(3, r.getCheckIn());
         ps.setString(4, r.getCheckOut());
         ps.setFloat(5, r.getRate());
         ps.setInt(6, r.getNumAdults());
         ps.setInt(7, r.getNumKids());
         result = ps.execute();



      }

      catch (SQLException e)
      {
         result = true;
         //e.printStackTrace();
      }

      finally
      {
         try
         {
            ps.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return result;

   }

   public Set<Manager> getManager(){

      Set<Manager> mset = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      try {
         ps = this.connection.prepareStatement("select ro.roomId, monthname(re.checkOut) as month,\n" +
                 "round(sum(datediff(re.checkOut, re.checkIn) * re.rate),0) as rev,\n" +
                 "count(monthname(re.checkIn)) as nRes\n" +
                 "from Reservations re\n" +
                 "join Rooms ro on re.roomId = ro.roomId\n" +
                 "group by month, ro.roomId\n" +
                 "order by roomId, monthname(month);");
         //ps.setString(1, inputName);
         //ps.setLong(2, inputPhone);
         resultSet = ps.executeQuery();
         mset = unpackManager(resultSet);
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
      //System.out.println("getManager");
      return mset;
   }

   private static Set<Manager> unpackManager(ResultSet rs)throws SQLException{
      Set<Manager> managerset = new HashSet<Manager>();

      while(rs.next()) {
         Manager manager = new Manager(
                 rs.getInt("roomId"),
                 rs.getString("month"),
                 rs.getInt("rev"),
                 rs.getInt("nRes"));
         managerset.add(manager);
      }
      return managerset;
   }

   public int getCurrentReservationId(int cId, int roomId){
      Set<Reservation> reservationSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      int ret = -1;

      //System.out.println(cId);
      //System.out.println(roomId);
      try
      {
         ps = this.connection.prepareStatement("SELECT MAX(R.resId) as mxResId FROM Reservations R where R.cId = ? and R.roomId = ? and R.cancelled = 0;");
         ps.setInt(1, cId);
         ps.setInt(2, roomId);
         resultSet = ps.executeQuery();
         try {
            while(resultSet.next()) {
               ret = resultSet.getInt("mxResId");
            }
         }catch (Exception e){
            ret = -1;
         }
      }

      catch (SQLException e)
      {
         e.printStackTrace();
      }

      finally
      {
         try
         {
            resultSet.close();
         }

         catch (SQLException e)
         {
            e.printStackTrace();
         }

         try
         {
            ps.close();
         } catch (SQLException e)
         {
            e.printStackTrace();
         }
      }

      return ret;
   }
}
