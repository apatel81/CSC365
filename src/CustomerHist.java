import java.util.Date;

public class CustomerHist {

        private int roomId;
        private Date checkIn;
        private Date checkOut;
        private Date datePaid;
        private float amountPaid;
        private int numAdults;
        private int numKids;
        private int cancelled;

        public int getroomId() {
            return roomId;
        }

        public Date getCheckIn() {
            return checkIn;
        }

        public Date getCheckOut() {
            return checkOut;
        }

        public Date getDatePaid() {
        return datePaid;
    }

        public float getAmountPaid() {
            return amountPaid;
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



        public CustomerHist(int roomId, Date checkIn, Date checkOut, Date datePaid, float amountPaid, int numAdults,
                            int numKids, int cancelled) {
            this.roomId = roomId;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.datePaid = datePaid;
            this.amountPaid = amountPaid;
            this.numAdults = numAdults;
            this.numKids = numKids;
            this.cancelled = cancelled;
        }

        public void printCustomerHist(){
            //System.out.println("Workin");
            System.out.println(String.format("%20d %20s %20s %20s %20f %20d %20d %20d",roomId,checkIn,checkOut,datePaid,
                    amountPaid,numAdults,numKids,cancelled));

        }

    }

