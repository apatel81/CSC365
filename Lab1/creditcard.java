import java.util.*;

public class creditcard
{
    protected static int cc_number = 0;
    protected String cc_type;
    protected int credit_limit;
    protected int cc_balance;
    protected boolean is_active = false;

    public creditcard(String cc_type, int credit_limit, int cc_balance)
    {
        this.cc_number ++;
        this.cc_type = cc_type;
        this.credit_limit = credit_limit;
        this.cc_balance = cc_balance;
        this.is_active = false; //is_active;
    }

    public static creditcard createCreditCard(Scanner in)
    {

        System.out.println("Enter Credit Card Type: ");
        String input_ccType = in.nextLine();

        System.out.println("Enter Credit Card Limit: ");
        int input_creditLimit = in.nextInt();
        in.nextLine();

        System.out.println("Enter Credit Card Balance: ");
        int input_balance = in.nextInt();
        while (input_balance > input_creditLimit)
        {
            System.out.println("Credit Card Balance Must be Less than Credit Card Limit");
            System.out.println("Enter Credit Card Balance: ");
            input_balance = in.nextInt();
        }

        in.nextLine();
        System.out.println("Success: Credit Card ID Number is " + creditcard.cc_number);
        System.out.println(" ");

        creditcard cc = new creditcard(input_ccType, input_creditLimit, input_balance);

        return cc;
    }

}
