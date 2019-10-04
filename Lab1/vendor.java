import java.util.*;

public class vendor
{
    protected static int id = 0;
    private String name;
    private String address;

    public vendor(String name, String address)
    {
        this.name = name;
        this.address = address;
        this.id ++;
    }

    public static vendor createVendor(Scanner in)
    {
        System.out.println("Enter Vendor Name: ");
        String input_name = in.nextLine();

        System.out.println("Enter Address: ");
        String input_address = in.nextLine();

        vendor v = new vendor(input_name, input_address);

        System.out.println("Vendor ID Number is " + (vendor.id -1));
        System.out.println(" ");

        return v;
    }
}
