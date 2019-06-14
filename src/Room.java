public class Room implements Comparable<Room>{
   private int roomId;
   private String roomName;
   private String bedType;
   private int numBeds;
   private int maxOcc;
   private float basePrice;
   private String decor;

   public int getRoomId() {
      return roomId;
   }

   public String getRoomName() {
      return roomName;
   }

   public String getBedType() {
      return bedType;
   }

   public int getNumBeds() {
      return numBeds;
   }

   public int getMaxOcc() {
      return maxOcc;
   }

   public float getBasePrice() {
      return basePrice;
   }

   public String getDecor() {
      return decor;
   }

   public Room(int roomId, String roomName, String bedType, int numBeds, int maxOcc, float basePrice, String decor) {
      this.roomId = roomId;
      this.roomName = roomName;
      this.bedType = bedType;
      this.numBeds = numBeds;
      this.maxOcc = maxOcc;
      this.basePrice = basePrice;
      this.decor = decor;
   }

   public void printRoom(){
      System.out.println(String.format("%-12s %-12.2f %-10s %-10s %-10d %-15s",roomName,basePrice,bedType,numBeds, maxOcc, decor));
   }


   public static void printRoomHeader()
   {
      String roomId = "Room ID";
      String roomName = "Room Name";
      String basePrice = "Base Price";
      String bedType = "Bed Type";
      String numBeds = "Number of Beds";
      String maxOcc = "Maximum Occupancy";
      String decor = "Decor";
      System.out.println(String.format("%-12s %-12s %-12s %-10s %-10s %-10s %-15s",roomId, roomName,basePrice,bedType,numBeds, maxOcc, decor));

   }
   public void printChangeRoom()
   {
      System.out.println(String.format("%-12d %-12s %-12.2f %-10s %-10s %-10d %-15s",roomId, roomName,basePrice,bedType,numBeds, maxOcc, decor));
   }

   @Override
   public int compareTo(Room r) {
      return this.roomName.compareTo(r.roomName); // or whatever property you want to sort
   }

}

