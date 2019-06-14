public class AvailObj {
   private Room r;
   private int roomCount;


   public Room getR() {
      return r;
   }

   public int getRoomCount() {
      return roomCount;
   }

   public AvailObj(Room r, int roomCount) {
      this.r = r;
      this.roomCount = roomCount;
   }

   public void printRoom(){
      System.out.println(String.format("",
              r.getRoomName(), r.getBedType(), r.getBasePrice(), r.getDecor(),  r.getMaxOcc(), this.roomCount ));
   }

}
