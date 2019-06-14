import java.sql.Connection;
import java.util.Set;
import java.sql.PreparedStatement;
import java.util.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;


public class RoomsDoa implements objDoa<Room>{
   private Connection connection;

   public RoomsDoa(Connection connection){
      this.connection = connection;
   }

   public Room getByKey(){

      return null;
   }

   public Set<Room> getAll(){
      Set<Room> rooms = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      try {
         ps = this.connection.prepareStatement("SELECT * FROM Rooms");
         resultSet = ps.executeQuery();
         rooms = unpackRooms(resultSet);
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
      return rooms;
   }

   public Set<Room> viewSpecificRooms(int totalOccupancy, int roomId){
      Set<Room> rooms = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      try
      {
         ps = this.connection.prepareStatement("SELECT * FROM Rooms WHERE maxOcc >= ? AND roomId <> ?");
         ps.setInt(1, totalOccupancy);
         ps.setInt(2, roomId);
         resultSet = ps.executeQuery();
         rooms = unpackRooms(resultSet);
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
         catch (SQLException e) {
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
      }
      return rooms;
   }


   public Boolean insert(Room r){
      return false;
   }

   public Boolean update(Room r){
      return false;
   }

   public Boolean delete(Room r){
      return false;
   }

   public Set<Room> searchAvailibilities(String checkIn, String checkOut, String bedType, int maxOcc, int maxP, float minP,
                                         String decor, int roomCount){
      Set<Room> custSet = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      try {
         ps = this.connection.prepareStatement("Select R.roomId,R.roomName, R.bedType, R.basePrice, R.Decor, R.maxOcc,R.numBeds\n" +
                 "   FROM Rooms R where R.roomId NOT IN\n" +
                 "   (Select RO.roomId FROM\n" +
                 "      Rooms RO JOIN Reservations RE\n" +
                 "            ON RE.roomId=RO.roomId\n" +
                 "                     where (RE.checkIn < STR_TO_DATE(?,'%Y-%m-%d'))\n" +
                 "                                 AND (STR_TO_DATE(?,'%Y-%m-%d') < RE.checkOut) AND RE.cancelled = 0 \n" +
                 "   )\n" +
                 "   and R.bedType = ? and R.maxOcc >= ?\n" +
                 "   and R.basePrice <= ? and R.basePrice >= ?\n" +
                 "   and R.decor = ?\n" +
                 "   ;");

         ps.setString(1, checkOut);
         ps.setString(2, checkIn);
         ps.setString(3, bedType);
         ps.setInt(4, maxOcc);
         ps.setFloat(5, maxP);
         ps.setFloat(6, minP);
         ps.setString(7, decor);

         resultSet = ps.executeQuery();
         custSet = unpackRooms(resultSet);
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
      return custSet;
   }

   private Set<Room> unpackRooms(ResultSet rs)throws SQLException{
         Set<Room> rooms = new HashSet<Room>();

         while(rs.next()) {
            Room room = new Room(
                    rs.getInt("roomId"),
                    rs.getString("roomName"),
                    rs.getString("bedType"),
                    rs.getInt("numBeds"),
                    rs.getInt("maxOcc"),
                    rs.getFloat("basePrice"),
                    rs.getString("decor"));
            rooms.add(room);
         }
         return rooms;
   }

   private Set<Room> unpackAvailRoom(ResultSet rs)throws SQLException{
      Set<Room> aObjs = new HashSet<Room>();

      while(rs.next()) {
          Room a = new Room(rs.getInt("roomId"),
                         rs.getString("roomName"),
                         rs.getString("bedType"),
                  rs.getInt("numBeds"),
                         rs.getInt("maxOcc"),
                         rs.getFloat("basePrice"),
                         rs.getString("decor"));
         aObjs.add(a);
      }
      return aObjs;
   }

   public Set<VRoom> getAllRoomViews(){
      Set<VRoom> vRooms = null;
      PreparedStatement ps = null;
      ResultSet resultSet = null;
      try {
         ps = this.connection.prepareStatement(
                 "Select R.roomName,RATE.score,R.basePrice,OC.oc,OC.dateOpen,OC.dateResAftOpen,R.bedType,R.numBeds,R.maxOcc,R.decor FROM\n" +
                         "   Rooms R\n" +
                         "JOIN\n" +
                         "   (\n" +
                         "   Select L1.roomId, ROUND(SUM(days)/180,2) as score FROM\n" +
                         "   (Select RO.roomId, COALESCE(SUM(DATEDIFF(checkOut,checkIn)),0) as days\n" +
                         "   FROM (Select * FROM Reservations where cancelled = 0 and checkIn > subdate(DATE(now()),180) and checkOut < DATE(now())) as R RIGHT JOIN Rooms RO\n" +
                         "   ON R.RoomId = RO.RoomId GROUP BY RO.roomId\n" +
                         "   UNION ALL\n" +
                         "   Select RO.roomId, COALESCE(DATEDIFF(R.checkOut,subdate(DATE(now()),180)),0) as days\n" +
                         "   FROM (SELECT * FROM Reservations\n" +
                         "   where checkOut > subdate(DATE(now()),180) and checkIn < subdate(DATE(now()),180) and cancelled = 0 ) as R RIGHT JOIN Rooms RO ON R.roomId = RO.roomId\n" +
                         "   UNION ALL\n" +
                         "   Select RO.roomId, COALESCE(DATEDIFF(Date(now()),R.checkIn),0) as days\n" +
                         "   FROM (SELECT * FROM Reservations\n" +
                         "   where checkOut > DATE(now()) and checkIn < DATE(now()) and cancelled = 0 ) as R RIGHT JOIN Rooms RO ON R.roomId = RO.roomId\n" +
                         "   )as L1\n" +
                         "   GROUP BY L1.roomId\n" +
                         "   )as RATE\n" +
                         "ON R.roomId = RATE.roomId\n" +
                         "JOIN\n" +
                         "(\n" +
                         "Select RO.roomId, IF(R.resId IS NULL, \"OPEN\", \"OCCUPIED\") as oc,\n" +
                         "   IF(R.resId is NULL, DATE(now()),\n" +
                         "   (SELECT IF(MIN(R2.checkOut) IS NULL, DATE(now()), MIN(R2.checkOut)) as nextOpening\n" +
                         "   FROM (Select * FROM Reservations where checkOut > DATE(now()) and cancelled = 0 and roomId = RO.roomId) as R2\n" +
                         "   where R2.checkOut <> (Select COALESCE(min(checkIn),\"0000-00-00\") FROM Reservations where checkOut > R2.checkOut and cancelled = 0 and roomId = RO.roomId))) as dateOpen,\n" +
                         "   (Select COALESCE(subdate(min(R5.checkIn),1), \"3000-00-00\")  FROM Reservations R5\n" +
                         "   where R5.roomId = RO.roomId and R5.checkIn > (SELECT IF(MIN(R2.checkOut) IS NULL, DATE(now()), MIN(R2.checkOut)) as nextOpening\n" +
                         "   FROM (Select * FROM Reservations where checkOut > DATE(now()) and cancelled = 0 and roomId = RO.roomId) as R2\n" +
                         "   where R2.checkOut <> (Select COALESCE(min(checkIn),\"0000-00-00\") FROM Reservations where checkOut > R2.checkOut and cancelled = 0 and roomId = RO.roomId))) as dateResAftOpen\n" +
                         "   FROM (Select * FROM Reservations R where R.checkIn < DATE(now()) and R.checkOut > DATE(now()) and R.cancelled = 0) as R\n" +
                         "   RIGHT JOIN Rooms RO ON R.roomId = RO.roomId\n" +
                         ") as OC\n" +
                         "ON OC.roomId = R.roomId\n" +
                         "Order by OC.oc desc,RATE.score desc;"

         );
         resultSet = ps.executeQuery();
         vRooms = unpackVRooms(resultSet);
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
      return vRooms;
   }

   private Set<VRoom> unpackVRooms(ResultSet rs) throws SQLException{
      Set<VRoom> vRooms = new HashSet<VRoom>();

      while(rs.next()) {
         VRoom room = new VRoom(
                 rs.getString("roomName"),
                 rs.getFloat("score"),
                 rs.getFloat("basePrice"),
                 rs.getString("oc"),
                 rs.getString("dateOpen"),
                 rs.getString("dateResAftOpen"),
                 rs.getString("bedType"),
                 rs.getInt("numBeds"),
                 rs.getInt("maxOcc"),
                 rs.getString("decor"));
         vRooms.add(room);
      }
      return vRooms;
   }
}




