import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        boolean done = false;

        ArrayList<customer> customers_list = new ArrayList<>();
        customerCollection customer_collection = new customerCollection();

        ArrayList<creditcard> cc_list = new ArrayList<>();
        creditcardCollection cc_collection = new creditcardCollection();

        ArrayList<ownership> owner_list = new ArrayList<>();
        ownershipCollection ownership_collection = new ownershipCollection();

        ArrayList<vendor> vendor_list = new ArrayList<>();
        vendorCollection vendor_collection = new vendorCollection();

        ArrayList<transaction> transaction_list = new ArrayList<>();
        transactionCollection transaction_collection = new transactionCollection();

        ArrayList<payment> payment_list = new ArrayList<>();
        paymentCollection payment_collection = new paymentCollection();

        while (!done)
        {
            System.out.println("u1. Create Customer ");
            System.out.println("u2. Create a New Credit Card");
            System.out.println("u3. Issue Credit Card Duplicate");
            System.out.println("u4. Cancel a Credit Card");
            System.out.println("u5. Activate a Credit Card");
            System.out.println("u6. Add a New Vendor");
            System.out.println("u7. Create a Transaction");
            System.out.println("u8. Pay off Credit Card");
            System.out.println("q1. Locate Customer");
            System.out.println("q2. Locate Credit Card Information by Customer ID Number or Customer SSN");
            System.out.println("q3. Locate Credit Card Information and Users by Credit Card ID Number");
            System.out.println("q4. See Transactions");
            System.out.println(" z. End Program");
            System.out.println(" ");
            System.out.println("Please Select a Letter: ");

            Scanner in = new Scanner(System.in);
            String letter = in.nextLine();

            if (letter.equals("u1"))
            {
                customer c = customer.createCustomer(in);
                customer_collection.addCustomer(c, customers_list);
            }

            else if (letter.equals("u2"))
            {
                if (customers_list.isEmpty())
                {
                    customer c = customer.createCustomer(in);
                    customer_collection.addCustomer(c, customers_list);
                }

                else
                {
                    creditcard cc = creditcard.createCreditCard(in);
                    cc_collection.addCC(cc, cc_list);

                    ownership o = ownership.createOwnership(in, customers_list, cc_list);
                    ownership_collection.addOwnership(o, owner_list);
                }
            }

            else if (letter.equals("u3"))
            {
                ownership o = ownership.createOwnership(in, customers_list, cc_list);
                ownership_collection.addOwnership(o, owner_list);
            }

            else if (letter.equals("u4"))
            {
                ownership_collection.cancelCC(in, owner_list);
            }

            else if (letter.equals("u5"))
            {
                cc_collection.activateCC(in, cc_list);
            }

            else if (letter.equals("u6"))
            {
                vendor v = vendor.createVendor(in);
                vendor_collection.addVendor(v, vendor_list);
            }

            else if (letter.equals("u7"))
            {
                transaction t = transaction.createTransaction_1(in, owner_list, vendor_list, cc_list, cc_collection);
                transaction_collection.addTransaction(t, transaction_list);
            }

            else if (letter.equals("u8"))
            {
                payment p = payment.createPayment_1(in, owner_list, cc_list);
                payment_collection.addPayment(p, payment_list);
            }

            else if (letter.equals("q1"))
            {
                customerCollection.locateCustomer(in, customers_list);
            }

            else if (letter.equals("q2"))
            {
                ownershipCollection.locateCC(in, owner_list, cc_list, customers_list);
            }

            else if (letter.equals("q3"))
            {
                creditcardCollection.locateCC(in, cc_list, owner_list);
            }

            else if (letter.equals("q4"))
            {
                transactionCollection.getTransactions(in, transaction_list);
            }

            else if (letter.equals("z"))
            {
                done = true;
            }

        }

    }

}
