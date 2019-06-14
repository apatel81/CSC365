import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.*;
import java.lang.*;
import static java.util.stream.Collectors.*;
import java.io.*;
import java.util.stream.Collectors;
import java.text.DateFormat;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Driver
{

    public static void main(String[] args)
    {
        boolean done = false;

        while (!done)
        {
            System.out.println(" ");
            System.out.println("1. View Rooms");
            System.out.println("2. Search Availabilities");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Change Reservation");
            System.out.println("5. View Guest History");
            System.out.println("6. Manager");
            System.out.println("7. End Program");
            System.out.println(" ");


            Scanner in = new Scanner(System.in);

            String input = in.nextLine();

            if (input.equals("1"))
            {
                viewRooms(in);
            }
            else if (input.equals("2"))
            {
                searchAvailabilities(in);
            }
            else if (input.equals("3"))
            {
                cancelBooking(in);
            }
            else if (input.equals("4"))
            {
                changeReservation(in);
            }
            else if (input.equals("5"))
            {
                viewGuestHistory(in);
            }
            else if (input.equals("6"))
            {
                manager(in);
            }else if (input.equals("7"))
            {
                endProgram(in);
            }else{
                System.out.println("Unrecognized Input");
            }
        }

    }

    private static void viewRooms(Scanner in) { //Baylor

        DaoManager doaMng = new DaoManager("connectionInfo.txt");

        //setup doa connection

        if (doaMng.setupConnection() == -1) {
            return;
        }

        //set autoCommitFalse
        if (doaMng.setAutoCommitFalse() == -1) {
            return;
        }

        //create connection object
        RoomsDoa roomDoa = doaMng.getRoomsDoa();

        //call connection obj function
        Set<VRoom> roomList = roomDoa.getAllRoomViews();


        System.out.println(String.format("%-12s %-12s %-8s %-15s %-13s %-12s %-8s %-10s %-10s %-8s",
                "Room Name", "Popularity ", "Price", "Open/Occupied", "Next Opening", "Open Until",
                "Bed Type", "Number Beds", "Max Occupancy", "Decor"));

        //commit query
        if (doaMng.commitChanges() == -1) {
            return;
        }

        //use set
        for (VRoom r : roomList) {
            r.printVRoom();
        }

        //set autoCommitTrue
        if (doaMng.setAutoCommitTrue() == -1) {
            return;
        }

        //close doa
        if (doaMng.close() == -1) {
            return;
        }

    }

    private static void searchAvailabilities(Scanner in){ //Baylor Whitehead
        //Collect Info
        searchParams sp = getSearchParams(in);

        //setup doa mangaer/set autocommit false
        DaoManager doaMng = null;
        if((doaMng = daoSetup("connectionInfo.txt"))==null){
            return;
        }
        RoomsDoa roomDoa = doaMng.getRoomsDoa();

        //get room list that meets choices
        Set<Room> roomSet = roomDoa.searchAvailibilities(
                sp.checkin, sp.checkout, sp.roomType, sp.numberOfOc, sp.maxPrice, sp.minPrice, sp.decor, sp.numRooms);
        if(doaMng.commitChanges() == -1){ return;}

        //pick Rooms
        roomsAndIndexes rI = null;
        if((rI = getRoomsAndIndexes(roomSet, in, sp)) == null){
            return;
        }

        //New User/Current User/New Card/Current Card
        CustAndCard cAndC = null;
        if ((cAndC= getCustAndCard(doaMng, in)) == null){
            return;
        }

        // -------------------- bookRoom
        if(!createResAndTran(in, doaMng, sp, cAndC, rI)){
            System.out.println("Reservation Failed");
        }

        //set autoCommitTrue
        if(doaMng.setAutoCommitTrue() == -1){return;}

        //close doa
        if(doaMng.close() == -1){return;}

    }

    private static void cancelBooking(Scanner in){
        int resId = getResId(in);

        //setup doa manager
        DaoManager DaoMng = new DaoManager("connectionInfo.txt");

        //setup doa connection
        if(DaoMng.setupConnection() == -1)
        {
            return;
        }

        //set autoCommitFalse
        if(DaoMng.setAutoCommitFalse() == -1)
        {
            return;
        }

        //create connection object
        ReservationsDoa resDoa = DaoMng.getReservationsDoa();

        //call connection obj func
        Set<Reservation> reservationList = resDoa.searchByResId(resId);

        //commit query
        if(DaoMng.commitChanges() == -1)
        {
            return;
        }

        if (reservationList.isEmpty())
        {
            searchForReservationByCustomerId(in, DaoMng, resDoa);
        }

        //use set
        else
        {
            for (Reservation r : reservationList)
            {
                if (r.cancelled == 1)
                {
                    System.out.println("Reservation is Already Cancelled");
                }

                else
                {
                    cancelBookingMessageANDinSQL(in, resId, DaoMng, resDoa);
                }
            }
        }

    }

    private static void changeReservation(Scanner in){
        int resId = getResId(in);

        //setup doa manager
        DaoManager DaoMng = new DaoManager("connectionInfo.txt");

        //setup doa connection
        if(DaoMng.setupConnection() == -1)
        {
            return;
        }

        //set autoCommitFalse
        if(DaoMng.setAutoCommitFalse() ==-1)
        {
            return;
        }

        //create connection object
        ReservationsDoa resDoa = DaoMng.getReservationsDoa();

        //call connection obj func
        Set<Reservation> reservationList = resDoa.searchByResId(resId);

        //commit query
        if(DaoMng.commitChanges() == -1)
        {
            return;
        }

        if (reservationList.isEmpty())
        {
            searchForReservationByCustomerId(in, DaoMng, resDoa);
        }

        else
        {
            for (Reservation r : reservationList)
            {
                if (r.cancelled == 1) {
                    System.out.println("Reservation Has Been Cancelled");
                }

                else
                {
                    getChangeReservationType(in, resId, DaoMng, resDoa);
                }
            }
        }

    }

    private static void viewGuestHistory(Scanner in){

        System.out.println("Please Enter the Name and Phone Number of the Guest.");
        System.out.println("Name: ");
        String inputName = in.nextLine() ;

        System.out.println("Phone Number: ");
        Long inputPhone = in.nextLong();
        in.nextLine();

        //System.out.println("Doa Manager Made");
        DaoManager doaMng2 = new DaoManager("connectionInfo.txt");

        //setup doa connection
        //System.out.println("Doa Connection Made");
        if(doaMng2.setupConnection() == -1){ return; }

        //System.out.println("Doa AutoCommit False");
        //set autoCommitFalse
        if( doaMng2.setAutoCommitFalse() ==-1){ return; }

        //System.out.println("Doa Connection Object Made");
        //create connection object
        CustomerDoa customerDoa = doaMng2.getCustomerDoa(); //change

        //System.out.println("Commit Query Made");
        //commit query
        if(doaMng2.commitChanges() == -1){ return;}

        //System.out.println("get Customer History");
        //call connection obj func
        Set<CustomerHist> customerHistoryList = customerDoa.getCustomerHistory(inputName, inputPhone);

        //System.out.println("print Customer Hist");
        //the print statement didn't work

        if (customerHistoryList.isEmpty()){System.out.println("Guest not found."); return;}

        //use set
        String rcol = "Room Id";
        String cicol = "Check In Date";
        String cocol = "Check Out Date";
        String dcol = "Date Paid";
        String acol = "Amount Paid";
        String ncol = "Number of Adults";
        String kcol = "Number of Kids";
        String ccol = "Cancelled";

        System.out.println(String.format("%20s %20s %20s %20s %20s %20s %20s %20s",rcol, cicol, cocol, dcol, acol,
                ncol, kcol, ccol));
        for(CustomerHist a : customerHistoryList){
            //System.out.println("Entered Loop.");
            a.printCustomerHist();
        }

        //set autoCommitTrue
        if(doaMng2.setAutoCommitTrue() == -1){return;}

        //close doa
        if(doaMng2.close() == -1){return;}

    }

    private static void manager(Scanner in) { //Ethan

        boolean ulogin = false;
        boolean login = false;

        System.out.println("Please Enter Login Credentials");
        System.out.println("User ID: ");
        String userID = in.nextLine();

        if (userID.equals("tkuboi")) {
            ulogin = true;
        }else{System.out.println("Incorrect Username"); return;}

        System.out.println("Password: ");
        String password = in.nextLine();

        if (password.equals("CSC365") & ulogin) {
            login = true;
        }else{System.out.println("Incorrect Password"); return;}

        if (login) {

            //System.out.println("Doa Manager Made");
            DaoManager doaMng2 = new DaoManager("connectionInfo.txt");

            //setup doa connection
            //System.out.println("Doa Connection Made");
            if (doaMng2.setupConnection() == -1) {
                return;
            }

            //System.out.println("Doa AutoCommit False");
            //set autoCommitFalse
            if (doaMng2.setAutoCommitFalse() == -1) {
                return;
            }

            //System.out.println("Doa Connection Object Made");
            //create connection object
            ReservationsDoa resDoa = doaMng2.getReservationsDoa(); //change

            //System.out.println("Commit Query Made");
            //commit query
            if (doaMng2.commitChanges() == -1) {
                return;
            }

            //System.out.println("get Customer History");
            //call connection obj func
            Set<Manager> managerSet = resDoa.getManager();


            //System.out.println("print Manager");
            //use set
            String rcol = "Room Id";
            String mcol = "Month";
            String recol = "Revenue";
            String ncol = "Number of Reservations";

            System.out.println(String.format("%20s %20s %20s %20s",rcol, mcol, recol, ncol));
            for (Manager m : managerSet) {
                m.printManager();
            }

            //set autoCommitTrue
            if (doaMng2.setAutoCommitTrue() == -1) {
                return;
            }

            //close doa
            if (doaMng2.close() == -1) {
                return;
            }
        }else{System.out.println("ElseBlock");}
    }

    private static void endProgram(Scanner in){
        System.exit(0);
    }



    //----------------support funcs----------------------------------------

    private static Room[] getRoomChoicesAsArray(Set<Room> roomSet){
        Room[] roomList = new Room[roomSet.size()];
        int i = 0;
        for(Room r :roomSet){
            roomList[i] = r;
            i+=1;
        }
        return roomList;
    }

    private static ArrayList<Integer> pickRoomsByIndex(Room[] roomList, Scanner in, int numRooms){
        ArrayList<Integer> roomChoiceIndexes = new ArrayList<Integer>();
        System.out.println(String.format("%-4s %-12s %-12s %-10s %-10s %-10s %-15s","Num","Room Name","Base Price" ,"Bed Type","Num Beds", "Max Occ", "Decor"));
        int i = 1;
        for(Room r: roomList){
            System.out.print(String.format("%-4d",i));
            r.printRoom();
            i+=1;
        }

        if(!checkBookRoom(in)){ return null; }

        int roomsLeft = numRooms;
        if(roomList.length <= numRooms){
            roomsLeft = roomList.length;
        }

        System.out.println("Pick " + roomsLeft + " Rooms");
        while(roomsLeft > 0){
            System.out.println("Pick a room using number to the left of name: ");
            int index = Integer.parseInt(in.nextLine()) - 1;
            if(!roomChoiceIndexes.contains(index) && index < roomList.length && index >= 0) {
                roomChoiceIndexes.add(index);
                roomsLeft -= 1;
            }else{
                System.out.println("That room has already been chosen or is not an option");
            }
        }

        System.out.println("All rooms have been chosen");

        return  roomChoiceIndexes;
    }

    private static class CustAndCard {
        protected Customer cust;
        protected CreditCard cc;

        public CustAndCard(CreditCard cc, Customer cust){
            this.cc = cc;
            this.cust = cust;
        }

    }

    private static CreditCard[] cardChoicesAsList(Customer c, DaoManager daoManager){
        CreditCardDoa ccDao = daoManager.getCreditCardDoa();
        Set<CreditCard> ccSet = ccDao.getAllCreditCardsOfUser(c);
        CreditCard[] ccList = new CreditCard[ccSet.size()];
        int i = 0;
        for(CreditCard cc: ccSet){
            ccList[i] = cc;
            i +=1;
        }
        return ccList;
    }

    private static Boolean askNewCard(Scanner in){
        while(true) {
            System.out.println("Card from above (a) or new credit card (n)");
            String choice = in.nextLine();
            if(choice.equals("a")){
                return false;
            }else if(choice.equals("n")){
                return true;
            }
            System.out.println("Unidentified Response. Please Try Again");
        }
    }

    private static long getCardNumber(DaoManager daoManager, Scanner in){
        long ccn = 0;

        while(true){
            System.out.println("What is the Credit Card Number");
            ccn = Long.parseLong(in.nextLine());
            if(String.valueOf(ccn).length() == 16){

                return ccn;
            }
            System.out.println("Not 16 numbers");
        }
    }

    private static String getAddress(Scanner in){
        System.out.println("What is the billing address");
        return in.nextLine();
    }

    private static String getHomeAddress(Scanner in){
        System.out.println("What is your address");
        return in.nextLine();
    }

    private static String getCardType(Scanner in){
        System.out.println("What is the card Type (MasterCard, Discover, etc)");
        return in.nextLine();
    }

    private static CreditCard addCard(DaoManager daoManager,Customer c, CreditCard[] ccList, Scanner in ){
        long ccn = getCardNumber(daoManager, in);

        //check if they already own card
        for(CreditCard cc: ccList){
            if(ccn == cc.getCcn()){
                System.out.println("That card is already on your account. Let's use that one");
                return cc;
            }
        }


        CreditCardDoa ccDao = daoManager.getCreditCardDoa();
        OwnershipDoa oDao = daoManager.getOwnershipDoa();
        int sec = getSecCode(in);
        String type = getCardType(in);
        String address = getAddress(in);
        String expDate = getExperationDate(in);

        CreditCard cc = new CreditCard(ccn,type,sec,expDate,address);

        if(ccDao.checkCardExists(ccn) == true){
            //add ownership
            Boolean ret = oDao.insert(new Ownership(c.getcId(),ccn));
            if(ret == true){
                System.out.println("Failed to add card 1");
                daoManager.rollbackChanges();
                return null;
            }

        }else{
            //add card and ownership
            Boolean ret = ccDao.insert(cc);
            if(ret == true){
                System.out.println("Failed to add card 2");
                daoManager.rollbackChanges();
                return null;
            }

            ret = oDao.insert(new Ownership(c.getcId(),ccn));
            if(ret == true){
                System.out.println("Failed to add card 3");
                daoManager.rollbackChanges();
                return null;
            }


        }

        daoManager.commitChanges();

        return cc;
    }

    private static int pickCardIndex(Scanner in, CreditCard[] ccList){
        int index = 0;
        while(true){
            System.out.println("Choose card by index");
            index = Integer.parseInt(in.nextLine());
            if(index <= ccList.length){
                return index -1;
            }
            System.out.println("That number is not an option");
        }
    }

    private static CreditCard pickCard(DaoManager daoManager, Customer c, Scanner in){
        CreditCard[] ccList = cardChoicesAsList(c, daoManager);
        CreditCard ccFinal = null;
        System.out.println(String.format("%-4s %-18s %10s","Num","Credit Card","Card Type"));
        int i = 1;
        for(CreditCard cc: ccList){
            System.out.print(i + "  ");
            cc.printCC();
            i +=1;
        }
        System.out.println();

        Boolean newCC = askNewCard(in);
        if(newCC == true){
            ccFinal = addCard(daoManager,c,ccList, in);
        }else{
            int indexOfCard = pickCardIndex(in, ccList);
            ccFinal = ccList[indexOfCard];
        }
        return ccFinal;
    }

    private static Boolean askHasAccount(Scanner in){
        while(true){
            System.out.println("Do you have an account");
            System.out.println("y - yes");
            System.out.println("n - no");
            String choice = in.nextLine();
            if(choice.equals("y")){
                return true;
            }else if(choice.equals("n")){
                return false;
            }
            System.out.println("Unidentified Response. Please Try Again");
        }
    }

    private static CustAndCard newAccount(Scanner in, DaoManager daoManager){
        String name = getUserName(in);
        String address = getHomeAddress(in);
        long phone = getUserPhoneNumber(in);

        CustomerDoa cDao = daoManager.getCustomerDoa();
        OwnershipDoa oDao = daoManager.getOwnershipDoa();

        CreditCard cc = null;
        Customer c = cDao.checkCustomerExists(name,phone);

        //if user already exsits log them in
        if(c != null){
            System.out.println("That Account already exists");
            cc = pickCard(daoManager,c,in);
            return new CustAndCard(cc,c);
        }

        c = new Customer(0,name,address,phone);
        Boolean ret = cDao.insert(c);
        if(ret == true){
            daoManager.rollbackChanges();
            System.out.println("New account creation failed");
            return null;
        }


        //check cust id
        c = cDao.checkCustomerExists(name, phone);
        if(c == null){
            System.out.println("New account creation failed 2");
            daoManager.rollbackChanges();
            return null;
        }

        cc = addCard(daoManager,c, new CreditCard[0], in);
        if(cc == null){
            System.out.println("Failed Card Addition");
            return null;
        }

        daoManager.commitChanges();

        return new CustAndCard(cc,c);
    }
    private static CustAndCard getCustAndCard(DaoManager daoManager, Scanner in){
        CreditCard cc = null;
        Customer cust = null;
        CustAndCard custAndCard = null;
        Boolean hasAccount = askHasAccount(in);

        if(hasAccount == true){
            //have user login

            cust= login(daoManager, in);
            if(cust == null){
                return null;
            }

            //select Card or add new one
            cc = pickCard(daoManager,cust,in);
            if(cc == null){
                return null;
            }
            return new CustAndCard(cc,cust);
        }else{
            return newAccount(in, daoManager);


        }

    }

    private static Customer login(DaoManager daoMng, Scanner in){
        String name = null;
        Long phoneNumber = null;
        CustomerDoa custDoa = daoMng.getCustomerDoa();
        while(true) {
            name = getUserName(in);
            phoneNumber = getUserPhoneNumber(in);


            Customer cust = custDoa.checkCustomerExists(name, phoneNumber);
            if(cust != null){

                return cust;
            }else{
                System.out.println("User does not exist - Try Again (any letter) or Quit (q)");
                String resp = in.nextLine();
                if(resp.equals("q")){
                    return null;
                }
            }
        }
    }

    private static searchParams getSearchParams(Scanner in){

        String checkin = getCheckInString(in);
        String checkout = getCheckOutString(in, checkin);
        int numDays = dateDiff(checkin,checkout);
        String roomType = getRoomType(in);
        String decor = getDecor(in);
        int minPrice = getMinPrice(in);
        int maxPrice = getMaxPrice(in, minPrice);
        int numberOfOc = getNumOcc(in);
        int numRooms = getNumRooms(in);
        return new searchParams(checkin,checkout,numDays,roomType,decor,minPrice,maxPrice,numberOfOc,numRooms);
    }

    private static class searchParams{
        protected String checkin;
        protected String checkout;
        protected int numDays;
        protected String roomType;
        protected String decor;
        protected int minPrice;
        protected int maxPrice;
        protected int numberOfOc;
        protected int numRooms;

        public searchParams(String checkin, String checkout, int numDays, String roomType, String decor, int minPrice,
                            int maxPrice, int numberOfOc, int numRooms){
            this.checkin = checkin;
            this.checkout = checkout;
            this.numDays = numDays;
            this.roomType = roomType;
            this.decor = decor;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.numberOfOc = numberOfOc;
            this.numRooms = numRooms;
        }
    }

    private static class roomsAndIndexes{
        protected ArrayList<Integer> roomIndexes;
        protected Room[] roomList;

        public roomsAndIndexes(ArrayList<Integer> roomIndexes, Room[] roomList) {
            this.roomIndexes = roomIndexes;
            this.roomList = roomList;
        }
    }

    private static roomsAndIndexes getRoomsAndIndexes(Set<Room> roomSet, Scanner in, searchParams sp){
        Room[] roomList = getRoomChoicesAsArray(roomSet);
        if(roomList == null || roomList.length == 0){
            System.out.println("Sorry no Rooms meet those specifications");
            return null;
        }
        ArrayList<Integer> roomIndexes = null;
        if( (roomIndexes = pickRoomsByIndex(roomList, in,sp.numRooms)) == null){
            System.out.println("Maybe Next Time");
            return null;
        }
        return new roomsAndIndexes(roomIndexes,roomList);
    }

    private static Boolean askToBook(Scanner in){
        System.out.println("Would your like to book this room (y)es or (n)o?");
        while (true) {
            try {
                String resp = in.nextLine();
                if(resp.equals("y")){
                    return true;
                }
                if(resp.equals("n")){
                    return false;
                }
            } catch (Exception e) {

            }
            System.out.println("Invalid response");
        }
    }
    private static Boolean createResAndTran(Scanner in, DaoManager daoMng, searchParams sp, CustAndCard cAndC, roomsAndIndexes rI){

        ReservationsDoa resDoa = daoMng.getReservationsDoa();
        TransactionDoa tranDoa = daoMng.getTransactionDoa();

        for (int index: rI.roomIndexes) {
            Room r = rI.roomList[index];

            //System.out.println("Room ID " + r.getRoomId());
            System.out.println("For Room " + r.getRoomName() + " with cost $" + r.getBasePrice()*sp.numDays);
            int adultsCount = getNumberAdults(in, sp.numberOfOc);
            int kidsCount = sp.numberOfOc - adultsCount;

            if(askToBook(in)){
                Reservation res = new Reservation(0, cAndC.cust.getcId(), r.getRoomId(), sp.checkin, sp.checkout, r.getBasePrice(), adultsCount, kidsCount, 0);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                if(resDoa.bookRoom(res)){
                    daoMng.rollbackChanges();
                    return false;
                }

                int resId = resDoa.getCurrentReservationId(cAndC.cust.getcId(), r.getRoomId());

                //System.out.println("RES ID" + resId);
                Transaction trans = new Transaction(0, cAndC.cc.getCcn(), resId, r.getBasePrice()*sp.numDays, dtf.format(LocalDateTime.now()));

                if(tranDoa.insert(trans)){
                    daoMng.rollbackChanges();
                    return false;
                }
                daoMng.commitChanges();

                System.out.println("BOOKED|  Reservation Id: " + resId +"  Room Name: " +  r.getRoomName() + "  Check In: " + sp.checkin
                        + "  Check Out: " + sp.checkout + "  Cost: $" + r.getBasePrice()*sp.numDays);
            }

        }



        return true;
    }

    private static DaoManager daoSetup(String filename){
        DaoManager doaMng = new DaoManager("connectionInfo.txt");
        if(doaMng.setupConnection() == -1){ return null; }
        if(doaMng.setAutoCommitFalse() ==-1){ return null; }
        return  doaMng;
    }

    private static int dateDiff(String checkIn, String checkOut){
        int diff = 0;

        checkIn = checkIn.replace("/","-");
        checkOut = checkOut.replace("/", "-");
        DateFormat f = new SimpleDateFormat("yyyy-mm-dd");
        try {
            Date dateIn = f.parse(checkIn);
            Date dateOut = f.parse(checkOut);

            long diffMilliseconds = dateOut.getTime()-dateIn.getTime();
            //System.out.println(diffMilliseconds);
            diff = (int)Math.abs(diffMilliseconds* 1.1574*Math.pow(10,-8));
            //System.out.println(diff);
        }catch (Exception e){
            System.out.println("Parse Error");
            return 0;
        }

        return diff+1;
    }


    private static int getNumberAdults(Scanner in, int numberOfOc){

        while(true) {
            System.out.println("How Many Adults out of " + numberOfOc + ". Remainder assumed kids");

            try {
                int adultsCount = Integer.parseInt(in.nextLine());
                if (adultsCount <= numberOfOc) {
                    return adultsCount;
                }
            } catch (Exception e) {
            }
            System.out.println("Bad input try again");
        }
    }

    private static String getExperationDate(Scanner in){
        String expDate = null;
        LocalDateTime now = LocalDateTime.now();
        while(true) {
            try {
                System.out.print("Please enter Experation Date (yyyy-mm-dd): ");
                expDate = in.nextLine();

                String[] inArray = expDate.split("-");
                if(inArray.length == 3
                        && String.valueOf(inArray[0]).length() == 4 //check right length
                        && String.valueOf(inArray[1]).length() == 2
                        && String.valueOf(inArray[2]).length() == 2
                        && inArray[0].matches("[0-9]+")  //check all chars are ints
                        && inArray[1].matches("[0-9]+")
                        && inArray[2].matches("[0-9]+")
                        && (Integer.parseInt(inArray[0]) > now.getYear() //check valid times
                        || (Integer.parseInt(inArray[0]) == now.getYear() && Integer.parseInt(inArray[1]) > now.getMonthValue() )
                        || (Integer.parseInt(inArray[0]) == now.getYear() && Integer.parseInt(inArray[1]) == now.getMonthValue() && Integer.parseInt(inArray[2]) >= now.getDayOfMonth()) ) ) {
                    break;
                }
            }catch (Exception e){
                System.out.println("Bad Date Try Again");
            }
            System.out.println("Bad Date Try Again");
        }
        return expDate;
    }

    private static int getSecCode(Scanner in){
        String secCode  =null;
        while(true){
            try{
                System.out.println("Enter your sec Code:");
                secCode = in.nextLine();
                if(secCode.length() == 3){
                    return Integer.parseInt(secCode);
                }
            }catch (Exception e){
                System.out.println("Bad card number");
            }
        }
    }

    private static String getUserName(Scanner in){
        System.out.println("What is your fist and last name:");
        return in.nextLine();
    }

    private static Long getUserPhoneNumber(Scanner in){
        String resp = null;
        while(true){
            try{
                System.out.println("What is your phone Number");
                resp = in.nextLine();
                if(resp.length() == 10 && Long.parseLong(resp) >= 0) {
                    return Long.parseLong(resp);
                }
                else{
                    System.out.println("Wrong Length");
                }
            }catch (Exception e){
                System.out.println("Bad Phone Number has letters ");
            }
        }

    }

    private static Boolean checkBookRoom(Scanner in){
        while(true) {
            System.out.println("Would you like to book a room(s)");
            System.out.println("y - Yes");
            System.out.println("n - No");
            String resp = in.nextLine();

            if(resp.equals("y")){
                return true;
            }else if(resp.equals("n")){
                return false;
            }else{
                System.out.println("Input not recognized: " + resp);
            }

        }

    }


    private static int getNumOcc(Scanner in){

        while(true) {
            try {
                System.out.println("Enter your group size per room");
                String num = in.nextLine();

                if(Integer.parseInt(num)>0){
                    return Integer.parseInt(num);
                }
            } catch (Exception e){
                System.out.println("Bad input");
            }
            System.out.println("Bad input");
        }
    }
    private static String getCheckInString(Scanner in){
        String checkIn = null;
        LocalDateTime now = LocalDateTime.now();
        while(true) {
            try {
                System.out.print("Please enter Check In Date (yyyy-mm-dd): ");
                checkIn = in.nextLine();

                String[] inArray = checkIn.split("-");
                if(inArray.length == 3
                        && String.valueOf(inArray[0]).length() == 4 //check right length
                        && String.valueOf(inArray[1]).length() == 2
                        && String.valueOf(inArray[2]).length() == 2
                        && inArray[0].matches("[0-9]+")  //check all chars are ints
                        && inArray[1].matches("[0-9]+")
                        && inArray[2].matches("[0-9]+")
                        && (Integer.parseInt(inArray[0]) > now.getYear() //check valid times
                        || (Integer.parseInt(inArray[0]) == now.getYear() && Integer.parseInt(inArray[1]) > now.getMonthValue() )
                        || (Integer.parseInt(inArray[0]) == now.getYear() && Integer.parseInt(inArray[1]) == now.getMonthValue() && Integer.parseInt(inArray[2]) >= now.getDayOfMonth()) ) ) {
                    break;
                }
            }catch (Exception e){
                System.out.println("Bad Date Try Again");
            }
            System.out.println("Bad Date Try Again");
        }
        return checkIn;
    }

    private static String getCheckOutString(Scanner in, String checkIn){
        String checkOut = null;
        while(true) {
            try {
                System.out.print("Please enter Check Out Date (yyyy-mm-dd): ");
                checkOut = in.nextLine();

                String[] inArray = checkIn.split("-");
                String[] outArray = checkOut.split("-");
                if(inArray.length == 3
                        && String.valueOf(outArray[0]).length() == 4 //check right length
                        && String.valueOf(outArray[1]).length() == 2
                        && String.valueOf(outArray[2]).length() == 2
                        && outArray[0].matches("[0-9]+")  //check all chars are ints
                        && outArray[1].matches("[0-9]+")
                        && outArray[2].matches("[0-9]+")
                        && ( (Integer.parseInt(outArray[0]) > Integer.parseInt(inArray[0])) //check valid times
                        || (Integer.parseInt(outArray[0]) == Integer.parseInt(inArray[0]) && Integer.parseInt(outArray[1]) > Integer.parseInt(inArray[1]) )
                        || (Integer.parseInt(outArray[0]) == Integer.parseInt(inArray[0]) && Integer.parseInt(outArray[1]) == Integer.parseInt(inArray[1])
                        && Integer.parseInt(outArray[2]) > Integer.parseInt(inArray[2])) ) ) {
                    break;
                }
            }catch (Exception e){
                System.out.println("Bad Date Try Again");
            }
            System.out.println("Bad Date Try Again");
        }
        return checkOut;
    }



    private static int getcheckin(Scanner in)
    {
        System.out.println("Enter Check-In Date (YYYYMMDD): ");
        int input_checkin = in.nextInt();
        in.nextLine();

        return input_checkin;
    }

    private static int getcheckout(Scanner in, int checkin)
    {
        System.out.println("Enter Check-Out Date (YYYYMMDD): ");
        int input_checkout = in.nextInt();
        in.nextLine();

        if (input_checkout <= checkin)
        {
            getcheckin(in);
        }

        return input_checkout;
    }

    private static String getSQLcheckin(Scanner in)
    {
        System.out.println("Enter Check-In Date (YYYYMMDD): ");
        String input_checkin = in.nextLine();
//        in.nextLine();

        return input_checkin;
    }

    private static String getSQLcheckout(Scanner in, String checkin)
    {
        System.out.println("Enter Check-Out Date (YYYYMMDD): ");
        String input_checkout = in.nextLine();

        if (Integer.parseInt(input_checkout) <= Integer.parseInt(checkin))
        {
            getcheckin(in);
        }

        return input_checkout;
    }


    private static String getRoomType(Scanner in)
    {
        while(true) {
            System.out.println("Please Select Number for One of Following Bed Types: ");
            System.out.println("    1. King");
            System.out.println("    2. Queen");
            System.out.println("    3. Double");

            int input_bedType = in.nextInt();
            in.nextLine();


            if (input_bedType == 1) {
                return "King";
            } else if (input_bedType == 2) {
                return "Queen";
            } else if (input_bedType == 3) {
                return "Double";
            }
            System.out.println("Bad input try again");
        }

    }

    private static String getDecor(Scanner in)
    {
        while(true) {
            System.out.println("Please Select Number for One of Following Room Decors: ");
            System.out.println("    1. Modern");
            System.out.println("    2. Rustic");

            int input_roomDecor = in.nextInt();
            in.nextLine();

            if (input_roomDecor == 1) {
                return "Modern";
            } else if (input_roomDecor == 2) {
                return "Rustic";
            }
        }
    }

    private static int getMinPrice(Scanner in)
    {
        System.out.println("Please Enter Lower Price Limit: ");

        int input_minPrice = in.nextInt();
        in.nextLine();

        if (input_minPrice < 0)
        {
            System.out.println("Min price must be above zero");
            return getMinPrice(in);
        }

        return input_minPrice;
    }

    private static int getMaxPrice(Scanner in, int minPrice)
    {
        System.out.println("Please Enter Max Price Limit: ");

        int input_maxPrice = in.nextInt();
        in.nextLine();

        if (input_maxPrice <= minPrice)
        {
            System.out.println("Max price must be greater than or equal to min price");
            return getMaxPrice(in,minPrice);
        }

        return input_maxPrice;
    }

    private static int getNumRooms(Scanner in)
    {
        System.out.println("Please Enter Number of Rooms: ");

        int input_numRooms = in.nextInt();
        in.nextLine();

        if (input_numRooms <= 0)
        {
            return getNumRooms(in);
        }

        return input_numRooms;
    }

    private static int getNumAdults(Scanner in)
    {
        System.out.println("Please Enter Number of Adults: ");

        int input_numAdults = in.nextInt();
        in.nextLine();

        if (input_numAdults <= 0)
        {
            getNumAdults(in);
        }

        return input_numAdults;
    }

    private static int getNumKids(Scanner in)
    {
        System.out.println("Please Enter Number of Children");

        int input_numKids = in.nextInt();
        in.nextLine();

        return input_numKids;
    }




    private static void bookRoom(Scanner in)
    {
        System.out.println("Would you like to Book a Room ");
        System.out.println("    1. Yes");
        System.out.println("    2. No");

        int input_bookRoom = in.nextInt();
        in.nextLine();

        if (input_bookRoom == 1)
        {
            isNewCustomer(in);
        }

        else
        {
            return;
        }
    }

    private static void isNewCustomer(Scanner in)
    {
        System.out.println("Are you a New Customer? ");
        System.out.println("    1. Yes");
        System.out.println("    2. No");

        int input_isNewCustomer = in.nextInt();
        in.nextLine();

        if (input_isNewCustomer == 1)
        {
            createNewCustomer(in);
        }

        else
        {
            check_ifCustomerExists(in);
        }
    }

    private static void createNewCustomer(Scanner in)
    {
        System.out.println("Please Enter Name: ");
        String input_Name = in.nextLine();

        System.out.println("Please Enter Address: ");
        String input_Address = in.nextLine();

        System.out.println("Please Enter Phone Number: ");
        Long input_Phone = in.nextLong();

//        CHECK IF I NEED TO WRITE IN.NEXTLINE() AFTER PHONE NUMBER INPUT

//        PASS OFF TO SQL --> store object and pass into function below

        createNewCreditCard(in);

    }

    private static void createNewCreditCard(Scanner in)
    {
        System.out.println("Please Enter Credit Card Number");
        Long input_ccn = in.nextLong();

        if (input_ccn.toString().length() <= 16)
        {
            createNewCustomer(in);
        }
        //        CHECK IF I NEED TO WRITE IN.NEXTLINE() AFTER PHONE NUMBER INPUT

        System.out.println("Please Enter Credit Card Type: ");
        String input_ccType = in.nextLine();

        System.out.println("Please Enter Expiration Date (YYYYMM): ");
        int input_expDate = in.nextInt();
        in.nextLine();

        System.out.println("Please Enter 3-Digit Security Code: ");
        int input_securityCode = in.nextInt();
        in.nextLine();

        System.out.println("Please Enter Billing Address: ");
        String input_address = in.nextLine();

//        PASS OFF TO SQL --> store object and pass into function below

        createOwnership(in);

    }

    private static void createNewCreditCard(Scanner in, long ccn)
    {
        System.out.println("Sorry Credit Card Number not on File");

        System.out.println("Please Enter Credit Card Type: ");
        String input_ccType = in.nextLine();

        System.out.println("Please Enter Expiration Date (YYYYMM): ");
        int input_expDate = in.nextInt();
        in.nextLine();

        System.out.println("Please Enter 3-Digit Security Code: ");
        int input_securityCode = in.nextInt();
        in.nextLine();

        System.out.println("Please Enter Billing Address: ");
        String input_address = in.nextLine();

//        PASS OFF TO SQL --> store object and pass into function below

        createOwnership(in);
    }


    private static void createOwnership(Scanner in)
    {
//        pass to SQL & write method to extract primary keys of new customer and new cc and store in ownership table
        showConfirmation(in);
    }

    private static void showConfirmation(Scanner in)
    {
//        write method to print details of confirmation --> checkIn date, checkOut date, price, duration of stay, numAdults, numKids
//        some attributes can be passed along from beginning of main but some may need to be taken from DAO objects
        System.out.println("Is Confirmation Correct? ");
        System.out.println("    1. Yes");
        System.out.println("    2. No");

        int input_Confirmation = in.nextInt();
        in.nextLine();

        if (input_Confirmation == 1)
        {
            createTransaction(in);
        }

        return;
    }

    private static void createTransaction(Scanner in)
    {
//        pass to SQL & write method to show similar attributes in transaction
    }


    private static void check_ifCustomerExists(Scanner in)
    {
        System.out.println("Please Enter Name: ");
        String input_Name = in.nextLine();

        System.out.println("Please Enter Phone Number: ");
        Long input_Phone = in.nextLong();

//        Pass to SQL to check if Customer exists or return object
//        If customer exists --> enterCCNumber(in, CUSTOMER ID)
//        If customer does not exist --> createNewCustomer

    }

    private static void enterCCNumber(Scanner in, int cId)
    {
        System.out.println("Please Enter Credit Card Number");
        Long input_ccn = in.nextLong();

        if (input_ccn.toString().length() <= 16)
        {
            enterCCNumber(in, cId);
        }

        checkOwnership(in, cId, input_ccn);
    }

    private static void checkOwnership(Scanner in, int cId, long ccn)
    {
//        write method to check customer id and ccn are in ownership table
//        if yes --> showConfirmation
//        if no --> createNewCreditCard(in, ccn);

    }


    private static int getResId(Scanner in)
    {
        System.out.println("Please Enter Reservation ID Number: ");
        System.out.println("If Reservation ID Number is Unknown, Please Enter 0");
        int input_resId = in.nextInt();
        in.nextLine();

        return input_resId;
    }


    private static void cancelBookingMessageANDinSQL(Scanner in, int resId, DaoManager DaoMng, ReservationsDoa resDoa)
    {
//        pass resId to SQL --> store that row as object & pass back here, then delete reservation from table
//        here show details of reservation that has been cancelled

        Set<Reservation> reservationList = resDoa.searchByResId(resId);

        boolean updateTable =  resDoa.setReservationToCancelled(resId);

        //commit query
        if(DaoMng.commitChanges() == -1)
        {
            return;
        }


//        DO WE NEED TO DO SOMETHING IN TRANSACTION THAT INDICATES WE MADE A REFUND --> MAYBE A NEGATIVE TRANSACTION??
        if (updateTable == false)
        {
            System.out.println("Refund Granted");

            for (Reservation r : reservationList) {
                r.printReservation();
            }


            if (DaoMng.setAutoCommitTrue() == -1) {
                return;
            }

            if (DaoMng.close() == -1) {
                return;
            }
        }
    }

    private static void searchForReservationByCustomerId(Scanner in, DaoManager DaoMng, ReservationsDoa resDoa)
    {
        System.out.println("Sorry Reservation ID Not Found");
        System.out.println("Please Enter your Name: ");
        String input_name = in.nextLine();

        System.out.println("Please Enter Phone Number: ");
        Long input_phoneNumber = in.nextLong();
        in.nextLine();


        Set<Reservation> reservationList = resDoa.showActiveReservations(input_name, input_phoneNumber);

        //commit query
        if(DaoMng.commitChanges() == -1)
        {
            return;
        }

        if (reservationList.isEmpty())
        {
            if (DaoMng.setAutoCommitTrue() == -1)
            {
                return;
            }

            System.out.println("Sorry, no Reservations Found");

            if(DaoMng.close() == -1)
            {
                return;
            }
        }

        else
        {
            for (Reservation res : reservationList)
            {
                res.printReservation();
            }

            System.out.println("Would you like to Cancel your Reservation or Make Changes to your Reservation?");
            System.out.println("Enter Number of Choice: ");
            System.out.println("    1. Cancel Reservation");
            System.out.println("    2. Make Changes to Reservation");
            System.out.println("    3. Return to Main Menu");
            int input_choice = in.nextInt();
            in.nextLine();

            if (input_choice == 1)
            {
                System.out.println("Please Enter the ID of the Reservation you would like to Cancel: ");
                int input_resId = in.nextInt();
                in.nextLine();

                cancelBookingMessageANDinSQL(in, input_resId, DaoMng, resDoa);
            }

            else if (input_choice == 2)
            {
                System.out.println("Please Enter the ID of the Reservation you would like to Make Changes to: ");
                int input_resId = in.nextInt();
                in.nextLine();

                getChangeReservationType(in, input_resId, DaoMng, resDoa);
            }

            else
            {
                return;
            }

        }
    }


    private static void getChangeReservationType(Scanner in, int resId, DaoManager DaoMng, ReservationsDoa resDoa)
    {
        System.out.println("Please Enter the Number for which you like to change: ");
        System.out.println("    1. Change Date");
        System.out.println("    2. Change Rooms");
        System.out.println("    3. Return to Main Menu");

        int input_changeReservationType = in.nextInt();
        in.nextLine();

        if (input_changeReservationType == 1)
        {
            changeReservationDate(in, resId, DaoMng, resDoa);
        }

        else if (input_changeReservationType == 2)
        {
            changeReservationRoom(in, resId, DaoMng, resDoa);
        }

        else
        {
            return;
        }
    }


    private static void changeReservationDate(Scanner in, int resId, DaoManager DaoMng, ReservationsDoa resDoa)
    {
        //Have resId of customer so its only 1 row
        Set<Reservation> reservationList = resDoa.searchByResId(resId);

        //Only one row so one iteration
        for (Reservation r : reservationList)
        {
            int roomId = r.getRoomId();
            float rate = r.getRate();
            int numAdults = r.getNumAdults();
            int numKids = r.getNumKids();

            String old_checkin = r.getCheckIn();
            String old_checkout = r.getCheckOut();

            String checkin = getSQLcheckin(in);
            String checkout = getSQLcheckout(in, checkin);


            String old_checkin_1 = old_checkin.substring(5,7) + "/" + old_checkin.substring(8,10) + "/" + old_checkin.substring(0,4);
            String old_checkout_1 = old_checkout.substring(5,7) + "/" + old_checkout.substring(8,10) + "/" + old_checkout.substring(0,4);
            String new_checkin_1 = checkin.substring(4,6) + "/" + checkin.substring(6,8) + "/" + checkin.substring(0,4);
            String new_checkout_1 = checkout.substring(4,6) + "/" + checkout.substring(6,8) + "/" + checkout.substring(0,4);


            long old_duration_of_stay = datediff(old_checkin_1, old_checkout_1);
            long new_duration_of_stay = datediff(new_checkin_1, new_checkout_1);

            if (old_duration_of_stay != new_duration_of_stay)
            {
                System.out.println("Length of Old Reservation is not the same as Attempted New Reservation");
                return;
            }


            Set<Reservation> availabilities = resDoa.checkAvailabilities(checkin, checkout, roomId);


            // means that there is an availability
            if (availabilities.isEmpty())
            {
                boolean updateDates = resDoa.updateReservationDates(resId, checkin, checkout);

                if(updateDates == false)
                {
                    System.out.println("New Reservation Successful");
                    Reservation.printChangeReservation();
                    System.out.println(String.format("%20d %20s %20s %20s %20.2f %20d %20d",resId, roomId, checkin, checkout, rate, numAdults, numKids));

                    if (DaoMng.setAutoCommitTrue() == -1) {
                        return;
                    }

                    if (DaoMng.close() == -1) {
                        return;
                    }
                }
            }

            else
            {
                System.out.println("Sorry Room is Occupied During Selected Dates");
                System.out.println("Please Select the Number of one of the Following Options: ");
                System.out.println("    1. Cancel Current Reservation");
                System.out.println("    2. Change Current Reservation");
                System.out.println("    3. Return to Main Menu");

                int input_newChoice = in.nextInt();
                in.nextLine();

                if (input_newChoice == 1)
                {
                    cancelBookingMessageANDinSQL(in, resId, DaoMng, resDoa);
                }

                else if (input_newChoice == 2)
                {
                    getChangeReservationType(in, resId, DaoMng, resDoa);
                }
                else
                {
                    return;
                }
            }
        }
    }

    private static long datediff(String checkin, String checkout)
    {
        long diff = 0;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

            java.util.Date firstDate = sdf.parse(checkin);
            Date secondDate = sdf.parse(checkout);

            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }

        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return diff;
    }

    private static void changeReservationRoom(Scanner in, int resId, DaoManager DaoMng, ReservationsDoa resDoa)
    {
        //Have resId of customer so its only 1 row
        Set<Reservation> reservationList = resDoa.searchByResId(resId);

        //Only one row so one iteration
        for (Reservation r : reservationList)
        {
            int roomId = r.getRoomId();
            float rate = r.getRate();
            int numAdults = r.getNumAdults();
            int numKids = r.getNumKids();
            String checkin = r.getCheckIn();
            String checkout = r.getCheckOut();

            //create connection object
            RoomsDoa roomDoa = DaoMng.getRoomsDoa();

            //call connection obj function
            Set<Room> roomsList = roomDoa.viewSpecificRooms(numAdults + numKids, roomId);

            Room.printRoomHeader();
            for (Room room : roomsList)
            {
                room.printChangeRoom();
            }
            int new_roomId = getRoomId(in);

            Set<Reservation> availabilities = resDoa.checkAvailabilities(checkin, checkout, new_roomId);

            //means there is an availability
            if (availabilities.isEmpty())
            {
                boolean updateRooms = resDoa.updateReservationRoom(resId, new_roomId);

                if (updateRooms == false)
                {
                    System.out.println("New Reservation Successful");
                    Reservation.printChangeReservation();
                    System.out.println(String.format("%20d %20s %20s %20s %20.2f %20d %20d",resId, new_roomId, checkin, checkout, rate, numAdults, numKids));

                    if (DaoMng.setAutoCommitTrue() == -1) {
                        return;
                    }

                    if (DaoMng.close() == -1) {
                        return;
                    }
                }
            }

            else
            {
                System.out.println(" ");
                System.out.println("Sorry, New Room is Occupied During Previous Dates");
                System.out.println("Please Select the Number of one of the Following Options: ");
                System.out.println("    1. Cancel Current Reservation");
                System.out.println("    2. Change Current Reservation");
                System.out.println("    3. Return to Main Menu ");

                int input_newChoice = in.nextInt();
                in.nextLine();

                if (input_newChoice == 1)
                {
                    cancelBookingMessageANDinSQL(in, resId, DaoMng, resDoa);
                }

                else if (input_newChoice == 2)
                {
                    getChangeReservationType(in, resId, DaoMng, resDoa);
                }

                else
                {
                    return;
                }
            }
        }
    }

    private static int getRoomId(Scanner in)
    {
        System.out.println("Please Enter Room Id of Room you would like to Select: ");
        int input_roomId = in.nextInt();
        in.nextLine();

        return input_roomId;
    }
  }