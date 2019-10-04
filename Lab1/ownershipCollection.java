import java.util.*;
import java.util.Scanner;

public class ownershipCollection
{

    public ownershipCollection(){}

    public void addOwnership(ownership o, ArrayList<ownership> owners)
    {
        owners.add(o);
    }

    public void cancelCC(Scanner in, ArrayList<ownership> owners)
    {
        System.out.println("Enter Credit Card ID Number to Cancel: ");
        int input_ccNumber = in.nextInt();
        in.nextLine();

        boolean made_change = false;
        for (ownership single_owner : owners)
        {
            if (single_owner.cc_number == input_ccNumber)
            {
                single_owner.is_current = false;
                System.out.println("Successfully Cancelled Credit Card ID Number: " + single_owner.cc_number);
                System.out.println(" ");

                made_change = true;
            }
        }

        if (!made_change)
        {
            System.out.println("Credit Card ID Number Does Not Exist");
            System.out.println(" ");
            cancelCC(in, owners);
        }
    }

    public static void locateCC(Scanner in, ArrayList<ownership> owner_list, ArrayList<creditcard> cc_list,
                                ArrayList<customer> customer_list)
    {
        System.out.println("Locate Customer By");
        System.out.println(" ");
        System.out.println("1. Customer ID Number");
        System.out.println("2. Customer SSN");
        System.out.println(" ");
        System.out.println("Please Enter 1 or 2: ");

        int input_number = in.nextInt();
        in.nextLine();

        if (input_number == 1)
        {
            locateCCByID(in, owner_list, cc_list);
        }

        else if (input_number == 2)
        {
            locateCCBySSN(in, owner_list, cc_list, customer_list);
        }

        else
        {
            locateCC(in, owner_list, cc_list, customer_list);
        }
    }

    public static void locateCCByID(Scanner in, ArrayList<ownership> owner_list, ArrayList<creditcard> cc_list)
    {
        System.out.println("Please Enter Customer ID Number: ");
        System.out.println(" ");
        int input_customerID = in.nextInt();
        in.nextLine();

        boolean found = false;
        for (ownership single_owner : owner_list)
        {
            if (single_owner.customer_id == input_customerID)
            {
                found = true;
                int cc_number = single_owner.cc_number;

                creditcard cc = cc_list.get(cc_number);
                System.out.println("Credit Card Number: " + cc_number);
                System.out.println("Credit Card Type: " + cc.cc_type);
                System.out.println("Credit Card Limit: " + cc.credit_limit);
                System.out.println("Credit Card Balance: " + cc.cc_balance);
                System.out.println("Active: " + cc.is_active);
                System.out.println(" ");
            }
        }

        if (!found)
        {
            System.out.println("Customer Does Not Own Credit Card. Please Try Again");
            System.out.println(" ");
            locateCCByID(in, owner_list, cc_list);
        }
    }

    public static void locateCCBySSN(Scanner in, ArrayList<ownership> owner_list, ArrayList<creditcard> cc_list,
                                     ArrayList<customer> customer_list)
    {
        System.out.println("Please Enter Customer SSN: ");
        System.out.println(" ");
        int input_ssn = in.nextInt();
        in.nextLine();

        boolean found_SSN = false;
        boolean found = false;
        for (customer single_customer : customer_list)
        {
            if (single_customer.ssn == input_ssn)
            {
                int customer_id = customer_list.indexOf(single_customer);
                found_SSN = true;

                for (ownership single_owner : owner_list)
                {
                    if (single_owner.customer_id == customer_id)
                    {
                        found = true;
                        int cc_number = single_owner.cc_number;

                        creditcard cc = cc_list.get(cc_number);
                        System.out.println("Credit Card Number: " + cc_number);
                        System.out.println("Credit Card Type: " + cc.cc_type);
                        System.out.println("Credit Card Limit: " + cc.credit_limit);
                        System.out.println("Credit Card Balance: " + cc.cc_balance);
                        System.out.println("Active: " + cc.is_active);
                        System.out.println(" ");
                    }
                }
            }
        }

        if (!found_SSN)
        {
            System.out.println("Customer SSN Does Not Exist. Please Try Again");
            System.out.println(" ");
            locateCCBySSN(in, owner_list, cc_list, customer_list);
        }

        if (!found)
        {
            System.out.println("Customer Does Not Own Credit Card. Please Try Again");
            System.out.println(" ");
            locateCCBySSN(in, owner_list, cc_list, customer_list);
        }

    }

}
