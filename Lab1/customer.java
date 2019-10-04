import java.awt.*;
import java.util.*;

public class customer
{
    protected int ssn;
    protected String name;
    protected String address;
    protected long phoneNumber;
    protected static int id = 0;

    public customer(int ssn, String name, String address, long phoneNumber)
    {
        this.ssn = ssn;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id ++;
    }

    public static customer createCustomer(Scanner in)
    {
        System.out.println("Enter Customer Name: ");
        String input_name = in.nextLine();

        System.out.println("Enter Customer SSN: ");
        int input_ssn = in.nextInt();
        in.nextLine();

        System.out.println("Enter Customer Address: ");
        String input_address = in.nextLine();

        System.out.println("Enter Customer 10 Digit Phone Number: ");
        long input_phoneNumber = in.nextLong();

        System.out.println("Success: Customer ID Number is " + customer.id);

        System.out.println(" ");
        customer c = new customer(input_ssn, input_name, input_address, input_phoneNumber);

        return c;
    }

}
