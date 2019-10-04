import java.util.*;

public class payment
{
    protected int customer_id;
    protected int cc_number;
    protected float amount;
    protected int date;

    public payment(int customer_id, int cc_number, float amount, int date)
    {
        this.customer_id = customer_id;
        this.cc_number = cc_number;
        this.amount = amount;
        this.date = date;
    }

    public static payment createPayment_1(Scanner in, ArrayList<ownership> owner_list, ArrayList<creditcard> cc_list) {
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
                    combination = true;
                    return createPayment_2(in, cc_list, input_ccID, input_customerID);
                }
            }
        }

        if (!combination) {
            System.out.println("Customer " + input_customerID + " Does Not Have Access to Credit Card " + input_ccID);
        }

        return createPayment_1(in, owner_list, cc_list);
    }

    public static payment createPayment_2(Scanner in, ArrayList<creditcard> cc_list, int cc_number, int customer_id)
    {
        System.out.println("Enter Date of Payment (YYYYMMDD): ");
        int input_date = in.nextInt();
        in.nextLine();

        creditcard cc = cc_list.get(cc_number);
        float input_amountPaid = cc.cc_balance;
        cc.cc_balance -= input_amountPaid;

        System.out.println("New Balance for Credit Card " + cc_number + " is " + cc.cc_balance);

        payment p = new payment(customer_id, cc_number, input_amountPaid, input_date);
        return p;
    }

}
