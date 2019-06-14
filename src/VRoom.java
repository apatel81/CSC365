public class VRoom {
   private String roomName;
   private float score;
   private float basePrice;
   private String occupied;
   private String dateOpen;
   private String dateResAftOpen;
   private String bedType;
   private int numBeds;
   private int maxOcc;
   private String decor;

   public String getRoomName() {
      return roomName;
   }

   public float getScore() {
      return score;
   }

   public float getBasePrice() {
      return basePrice;
   }

   public String getOccupied() {
      return occupied;
   }

   public String getDateOpen() {
      return dateOpen;
   }

   public String getDateResAftOpen() {
      return dateResAftOpen;
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

   public String getDecor() {
      return decor;
   }

   public VRoom(String roomName, float score, float basePrice, String occupied, String dateOpen, String dateResAftOpen, String bedType, int numBeds, int maxOcc, String decor) {
      this.roomName = roomName;
      this.score = score;
      this.basePrice = basePrice;
      this.occupied = occupied;
      this.dateOpen = dateOpen;
      this.dateResAftOpen = dateResAftOpen;
      this.bedType = bedType;
      this.numBeds = numBeds;
      this.maxOcc = maxOcc;
      this.decor = decor;
   }

   public void printVRoom(){
      System.out.println(String.format("%-12s %-12.2f %-8.2f %-15s %-13s %-12s %-8s %-11d %-13d %-8s",
              this.roomName,this.score, this.basePrice, this.occupied, this.dateOpen, this.dateResAftOpen,
              this.bedType, this.numBeds, this.maxOcc, this.decor));
   }
}
