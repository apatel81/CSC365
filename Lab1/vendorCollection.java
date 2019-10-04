import java.util.*;

public class vendorCollection
{
    public vendorCollection() {}

    public void addVendor(vendor v, ArrayList<vendor> vendors)
    {
        vendors.add(vendor.id - 1, v);
    }
}
