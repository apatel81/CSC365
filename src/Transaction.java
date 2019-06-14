public class Transaction {
   private int tId;
   private long ccn;
   private int resId;
   private float amountPaid;
   private String datePaid;

   public int gettId() {
      return tId;
   }



   public long getCcn() {
      return ccn;
   }

   public int getResId() {
      return resId;
   }

   public float getAmountPaid() {
      return amountPaid;
   }

   public String getDatePaid() {
      return datePaid;
   }

   public Transaction(int tId, long ccn, int resId, float amountPaid, String datePaid) {
      this.tId = tId;
      this.ccn = ccn;
      this.resId = resId;
      this.amountPaid = amountPaid;
      this.datePaid = datePaid;
   }
}
