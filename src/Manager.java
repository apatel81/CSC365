public class Manager {

    private int roomId;
    private String month;
    private int rev;
    private int nRes;

    public int getroomId() {
        return roomId;
    }

    public String getmonth() {
        return month;
    }

    public float getRev() {
        return rev;
    }

    public int getnRes() {
        return nRes;
    }


    public Manager(int roomId, String month, int rev, int nRes) {
        this.roomId = roomId;
        this.month = month;
        this.rev = rev;
        this.nRes = nRes;
    }

    public void printManager(){
        //System.out.println("Workin");
        System.out.println(String.format("%20d %20s %20d %20d",roomId, month, rev, nRes));

    }


}
