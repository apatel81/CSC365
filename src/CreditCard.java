public class CreditCard {
   private long ccn;
   private String type;
   private int secCode;
   private String expDate;
   private String bill_address;

   public long getCcn() {
      return ccn;
   }

   public String getType() {
      return type;
   }

   public int getSecCode() {
      return secCode;
   }

   public String getExpDate() {
      return expDate;
   }

   public String getBill_adress() {
      return bill_address;
   }

   public CreditCard(long ccn, String type, int secCode, String expDate, String bill_address) {
      this.ccn = ccn;
      this.type = type;
      this.secCode = secCode;
      this.expDate = expDate;
      this.bill_address = bill_address;
   }

   public void printCC(){
      System.out.println(String.format("  xxxx xxxx xxxx %-4s %10s", String.valueOf(this.ccn).substring(12,16), this.type));
   }

}
