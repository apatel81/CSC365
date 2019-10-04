import java.util.*;

public class ownership
{
    protected int customer_id;
    protected int cc_number;
    protected boolean is_current = true;

    public ownership(int customer_id, int cc_number)
    {
        this.customer_id = customer_id;
        this.cc_number = cc_number;
        this.is_current = true; //= is_current;
    }

    public static ownership createOwnership(Scanner in, ArrayList<customer> customer_list, ArrayList<creditcard> cc_list)
    {
        System.out.println("Enter Customer ID Number: ");
        int input_customerID = in.nextInt();
        in.nextLine();

        if (input_customerID <= customer_list.size() - 1)
        {

            System.out.println("Enter Credit Card ID Number: ");
            int input_ccNumber = in.nextInt();
            in.nextLine();

            if (input_ccNumber <= cc_list.size() - 1)
                {
                    System.out.println("Success");
                    System.out.println(" ");

                    ownership o = new ownership(input_customerID, input_ccNumber);
                    return o;
                }

            else
            {
                System.out.println("Credit Card ID Number Does Not Exist");
            }
        }

        else
        {
            System.out.println("Customer ID Number Does Not Exist");
        }

        return createOwnership(in, customer_list, cc_list);

    }
}
