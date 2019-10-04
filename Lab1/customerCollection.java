import java.util.*;

public class customerCollection
{

    public customerCollection() {}

    public void addCustomer(customer c, ArrayList<customer> customers)
    {
        if (customers.isEmpty())
        {
            customers.add(customer.id - 1, c);
        }

        else
        {
            for (customer single_customer : customers)
            {
                if (single_customer.ssn == c.ssn)
                {
                    System.out.println("Error: Person with same SSN already exists");
                    System.out.println("Please Try Again");
                    System.out.println(" ");
                    customer.id -= 1;
                    return;
                }
            }

            customers.add(customer.id - 1, c);
        }
    }

    public static void locateCustomer(Scanner in, ArrayList<customer> customer_list)
    {
        System.out.println("Locate Customer By: ");
        System.out.println(" ");
        System.out.println("1. Customer ID Number");
        System.out.println("2. Customer SSN");
        System.out.println(" ");
        System.out.println("Please Enter 1 or 2: ");

        int input_number = in.nextInt();
        in.nextLine();

        if (input_number == 1)
        {
            locateByID(in, customer_list);
        }

        else if (input_number == 2)
        {
            locateBySSN(in, customer_list);
        }

        else
        {
            locateCustomer(in, customer_list);
        }
    }

    public static void locateByID(Scanner in, ArrayList<customer> customer_list)
    {
        System.out.println("Please Enter Customer ID Number: ");
        System.out.println(" ");
        int input_customerID = in.nextInt();
        in.nextLine();

        if (input_customerID <= customer_list.size() - 1)
        {
            customer c = customer_list.get(input_customerID);
            System.out.println("Customer Name: " + c.name);
            System.out.println("Customer SSN: " + c.ssn);
            System.out.println("Customer Address: " + c.address);
            System.out.println("Customer Phone Number: " + c.phoneNumber);
            System.out.println(" ");
        }

        else
        {
            System.out.println("Customer ID Number Does Not Exist. Please Try Again");
            System.out.println(" ");
            locateByID(in, customer_list);
        }

    }

    public static void locateBySSN(Scanner in, ArrayList<customer> customer_list)
    {
        System.out.println("Please Enter Customer SSN: ");
        System.out.println(" ");
        int input_ssn = in.nextInt();
        in.nextLine();

        boolean found = false;
        for (customer single_customer : customer_list)
        {
            if (single_customer.ssn == input_ssn)
            {
                found = true;
                System.out.println("Customer Name: " + single_customer.name);
                System.out.println("Customer SSN: " + single_customer.ssn);
                System.out.println("Customer Address:" + single_customer.address);
                System.out.println("Customer Phone Number: " + single_customer.phoneNumber);
            }
        }

        if (!found)
        {
            System.out.println("Customer SSN Does Not Exist. Please Try Again");
            System.out.println(" ");
            locateBySSN(in, customer_list);
        }

    }
}
