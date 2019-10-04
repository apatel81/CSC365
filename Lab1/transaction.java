import java.util.*;

public class transaction
{
    protected int customer_id;
    protected int cc_number;
    protected int vendor_id;
    protected float amount_spent;
    protected int date;

    public transaction(int customer_id, int cc_number, int vendor_id, float amount_spent, int date)
    {
        this.customer_id = customer_id;
        this.cc_number = cc_number;
        this.vendor_id = vendor_id;
        this.amount_spent = amount_spent;
        this.date = date;
    }

    public static transaction createTransaction_1(Scanner in, ArrayList<ownership> owner_list, ArrayList<vendor> vendor_list,
                                                  ArrayList<creditcard> cc_list, creditcardCollection cc_collection)
    {
       System.out.println("Enter Customer ID Number: ");
       int input_customerID = in.nextInt();
       in.nextLine();

       System.out.println("Enter Credit Card ID Number: ");
       int input_ccID = in.nextInt();
       in.nextLine();

       boolean combination = false;
       for (ownership single_owner : owner_list)
       {
           if (single_owner.customer_id == input_customerID)
           {
               if (single_owner.cc_number == input_ccID)
               {
                   creditcard cc = cc_list.get(input_ccID);

                   if (cc.is_active)
                   {
                       combination = true;
                       return createTransaction_2(in, input_customerID, input_ccID, vendor_list, cc_list);
                   }

                   else
                   {
                       System.out.println("Please Activate Credit Card");
                       System.out.println(" ");
                       cc_collection.activateCC(in, cc_list);
                       return createTransaction_1(in, owner_list, vendor_list, cc_list, cc_collection);
                   }
               }
           }

       }

       if (!combination)
       {
           System.out.println("Customer " + input_customerID + " Does Not Have Access to Credit Card " + input_ccID);
           System.out.println(" ");
       }

       return createTransaction_1(in, owner_list, vendor_list, cc_list, cc_collection);
    }

    public static transaction createTransaction_2(Scanner in, int customer_id, int cc_number,
                                                  ArrayList<vendor> vendor_list, ArrayList<creditcard> cc_list)
    {
        System.out.println("Enter Vendor ID Number: ");
        int input_vendorID = in.nextInt();
        in.nextLine();

        if (input_vendorID <= vendor_list.size() - 1)
        {
            System.out.println("Enter Amount Charged: ");
            float input_amountSpent = in.nextFloat();
            in.nextLine();

            System.out.println("Enter Date of Transaction (YYYYMMDD): ");
            int input_date = in.nextInt();
            in.nextLine();

            creditcard cc = cc_list.get(cc_number);
            cc.cc_balance += input_amountSpent;

            System.out.println("New Balance for Credit Card " + cc_number + " is " + cc.cc_balance);
            System.out.println(" ");

            transaction t = new transaction(customer_id, cc_number, input_vendorID, input_amountSpent, input_date);
            return t;
        }

        else
        {
            System.out.println("Vendor ID Number Does Not Exist");
            System.out.println(" ");
        }
        return createTransaction_2(in, customer_id, cc_number, vendor_list, cc_list);
    }

}
