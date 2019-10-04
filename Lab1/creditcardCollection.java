import java.util.*;

public class creditcardCollection
{
    public creditcardCollection() {}

    public void addCC(creditcard input_cc, ArrayList<creditcard> creditcards)
    {
        if (creditcards.isEmpty())
        {
            creditcards.add(creditcard.cc_number - 1, input_cc);
        }

        else
        {
            int count = 0;
            for (creditcard single_cc : creditcards)
            {
                if (count == creditcard.cc_number)
                {
                    System.out.println("Error: Credit Card ID Number already exists");
                    System.out.println("Please Try Again");
                    System.out.println(" ");
                    creditcard.cc_number -= 1;
                    return;
                }
                count += 1;
            }

            creditcards.add(creditcard.cc_number - 1, input_cc);
        }
    }

    public void activateCC(Scanner in, ArrayList<creditcard> cc_list)
    {
        System.out.println("Which Credit Card Do You Want to Activate?");
        System.out.println("Enter Credit Card ID Number: ");
        System.out.println(" ");
        int input_ccNumber = in.nextInt();
        in.nextLine();

        if (input_ccNumber <= cc_list.size() - 1)
        {
            creditcard cc = cc_list.get(input_ccNumber);
            cc.is_active = true;
            System.out.println("Successfully Activated Credit Card ID Number: " + input_ccNumber);
            System.out.println(" ");
        }

        else
        {
            System.out.println("Credit Card ID Number Does Not Exist");
            System.out.println(" ");
            System.out.println(" ");
            activateCC(in, cc_list);
        }
    }

    public static void locateCC(Scanner in, ArrayList<creditcard> cc_list, ArrayList<ownership> owner_list)
    {
        System.out.println("Enter Credit Card Number: ");
        int input_ccNumber = in.nextInt();
        in.nextLine();

        if (input_ccNumber <= cc_list.size() - 1)
        {
            creditcard cc = cc_list.get(input_ccNumber);

            System.out.println("Credit Card Type: " + cc.cc_type);
            System.out.println("Credit Card Limit: " + cc.credit_limit);
            System.out.println("Credit Card Balance: " + cc.cc_balance);
            System.out.println("Active: " + cc.is_active);
            System.out.println(" ");

            boolean owned_by = false;
            for (ownership single_owner : owner_list)
            {
                if (single_owner.cc_number == input_ccNumber)
                {
                    owned_by = true;
                    System.out.println("Is Owned By Customer: " + single_owner.customer_id);
                }
            }

            if (!owned_by)
            {
                System.out.println("Credit Card " + input_ccNumber + "is not Owned by Anyone");
                System.out.println(" ");
            }
        }

        else
        {
            System.out.println("Credit Card ID Number Does Not Exist. Please Try Again");
            System.out.println(" ");
            locateCC(in, cc_list, owner_list);
        }
    }
}
