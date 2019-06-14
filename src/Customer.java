import com.sun.scenario.effect.impl.sw.sse.SSEBlend_ADDPeer;

public class Customer{
   private int cId;
   private String name;
   private String address;
   private long phone;

   public int getcId() {
      return cId;
   }

   public String getName() {
      return name;
   }

   public String getAddress() {
      return address;
   }

   public long getPhone() {
      return phone;
   }

   public Customer(int cId, String name, String address, long phone) {
      this.cId = cId;
      this.name = name;
      this.address = address;
      this.phone = phone;
   }

   public void printCustomer(){
      System.out.println("Name = " + this.getName() + " ID: " + this.getcId());
   }
}