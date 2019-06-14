import java.sql.Date;

public class Reservation {
   private int cId;
   private int roomId;
   private float rate;
   private int numAdults;
   private int numKids;
   protected int resId;
   protected int cancelled = 0;
   protected String checkIn;
   protected String checkOut;

   public int getResId() {
      return resId;
   }

   public int getcId() {
      return cId;
   }

   public int getRoomId() {
      return roomId;
   }

   public String getCheckIn(){return checkIn;}

   public String getCheckOut(){return checkOut;}

   public float getRate() {
      return rate;
   }

   public int getNumAdults() {
      return numAdults;
   }

   public int getNumKids() {
      return numKids;
   }

   public int getCancelled() {
      return cancelled;
   }

   public void printReservation(){
      String RESID = "Reservation ID";
      String ROOMID = "Room ID";
      String CHECKIN = "Check In";
      String CHECKOUT = "Check Out";
      String RATE = "Rate";
      String NUMADULTS = "Number of Adults";
      String NUMKIDS = "Number of Kids";
      System.out.println(String.format("%20s %20s %20s %20s %20s %20s %20s", RESID, ROOMID, CHECKIN, CHECKOUT, RATE,
              NUMADULTS, NUMKIDS));
      System.out.println(String.format("%20d %20s %20s %20s %20.2f %20d %20d",resId, roomId, checkIn, checkOut, rate, numAdults, numKids));
   }

   public static void printChangeReservation(){
      String RESID = "Reservation ID";
      String ROOMID = "Room ID";
      String CHECKIN = "Check In";
      String CHECKOUT = "Check Out";
      String RATE = "Rate";
      String NUMADULTS = "Number of Adults";
      String NUMKIDS = "Number of Kids";
      System.out.println(String.format("%20s %20s %20s %20s %20s %20s %20s", RESID, ROOMID, CHECKIN, CHECKOUT, RATE,
              NUMADULTS, NUMKIDS));
   }

   public Reservation(int resId, int cId, int roomId, String checkIn, String checkOut, float rate, int numAdults, int numKids, int cancelled)
   {
      this.resId = resId;
      this.cId = cId;
      this.roomId = roomId;
      this.checkIn = checkIn;
      this.checkOut = checkOut;
      this.rate = rate;
      this.numAdults = numAdults;
      this.numKids = numKids;
      this.cancelled = cancelled;
   }
}
