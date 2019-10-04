import java.util.*;

public class transactionCollection
{
    public transactionCollection() {}

    public void addTransaction(transaction t, ArrayList<transaction> transactions)
    {
        transactions.add(t);
    }

    public static void getTransactions(Scanner in, ArrayList<transaction> transaction_list)
    {
        System.out.println("Enter Credit Card ID Number: ");
        int input_ccNumber = in.nextInt();
        in.nextLine();

        System.out.println("Enter Start Date (YYYYMMDD): ");
        int input_start = in.nextInt();
        in.nextLine();

        System.out.println("Enter End Date (YYYYMMDD): ");
        int input_end = in.nextInt();
        in.nextLine();

        boolean found = false;
        boolean withinDate = false;

        for (transaction single_transaction : transaction_list)
        {
            if (single_transaction.cc_number == input_ccNumber)
            {
                found = true;

                if (single_transaction.date >= input_start & single_transaction.date <= input_end)
                {
                    withinDate = true;
                    System.out.println("Customer ID Number: " + single_transaction.customer_id);
                    System.out.println("Credit Card ID Number: " + single_transaction.cc_number);
                    System.out.println("Vendor ID Number: " + single_transaction.vendor_id);
                    System.out.println("Amount Spent: " + single_transaction.amount_spent);
                    System.out.println("Date: " + single_transaction.date);
                    System.out.println(" ");
                }
            }
        }

        if (!found)
        {
            System.out.println("Credit Card Was Never Used. Please Specify Different Credit Card");
            System.out.println(" ");
            getTransactions(in, transaction_list);
        }

        if (!withinDate & found)
        {
            System.out.println("There Were No Transactions Within Date Specified. Please Try Again");
            System.out.println(" ");
            getTransactions(in, transaction_list);
        }
    }
}
