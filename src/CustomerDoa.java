import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CustomerDoa implements objDoa<Customer>{
   private Connection connection;

   public CustomerDoa(Connection connection){
      this.connection = connection;
   }

   public Customer getByKey(){

      return null;
   }

    public Set<Customer> getAll(){return null;}


   public Set<Customer> getCustomer(String inputName, long inputPhone){

       Set<Customer> cust2 = null;
       PreparedStatement ps = null;
       ResultSet resultSet = null;
       try {
           ps = this.connection.prepareStatement("select * from Reservations r \n" +
                   "join Customer c on c.cId = r.cId\n" +
                   "join Transaction t on t.cId = r.cId\n" +
                   "where c.name = ? and c.phone = ? ;");
           ps.setString(1, inputName);
           ps.setLong(2, inputPhone);
           resultSet = ps.executeQuery();
           cust2 = unpackCustomer(resultSet);
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try {
               resultSet.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
           try {
               ps.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       return cust2;
   }

    public Set<CustomerHist> getCustomerHistory(String inputName, long inputPhone){

       //System.out.println("input Name "+ inputName);
        //System.out.println("input Phone "+ inputPhone);

        Set<CustomerHist> cust3 = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = this.connection.prepareStatement("select * from Reservations r \n" +
                    "join Customer c on c.cId = r.cId\n" +
                    "join Transaction t on t.resId = r.resId\n" +
                    "where c.name = ? and c.phone = ? ;");
            ps.setString(1, inputName);
            ps.setLong(2, inputPhone);
            resultSet = ps.executeQuery();
            cust3 = unpackCustomerHist(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("getCustomerHistory");
        //System.out.println("iteration thing"+ cust3.iterator().next());
        return cust3;
    }

   public Boolean insert(Customer c){
       Boolean successful = false;
       PreparedStatement ps = null;


       try {
           ps = this.connection.prepareStatement(
                   "INSERT INTO Customer (cId,name,address,phone) VALUES (?, ?, ?, ?)");
           ps.setLong(1, c.getcId());
           ps.setString(2, c.getName());
           ps.setString(3, c.getAddress());
           ps.setLong(4, c.getPhone());
           successful = ps.execute();
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try {
               ps.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       return successful;
   }

   public Boolean update(Customer c){
      return false;
   }

   public Boolean delete(Customer c){
      return false;
   }

    private Set<Customer> unpackCustomer(ResultSet rs)throws SQLException{
        Set<Customer> customerset = new HashSet<Customer>();

        while(rs.next()) {
            Customer cust = new Customer(
                    rs.getInt("cId"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getLong("phone"));
            customerset.add(cust);
        }
        return customerset;
    }

    private Set<CustomerHist> unpackCustomerHist(ResultSet rs)throws SQLException{
        Set<CustomerHist> customerhistset = new HashSet<CustomerHist>();

        while(rs.next()) {
            CustomerHist custHist = new CustomerHist(
                    rs.getInt("roomId"),
                    rs.getDate("checkIn"),
                    rs.getDate("checkOut"),
                    rs.getDate("datePaid"),
                    rs.getFloat("amountPaid"),
                    rs.getInt("numAdults"),
                    rs.getInt("numKids"),
                    rs.getInt("cancelled"));
            customerhistset.add(custHist);
        }
        //System.out.println("unpacked");
        return customerhistset;
    }

    public Customer checkCustomerExists(String name, Long phone){
       Set<Customer> cust1 = null;
       PreparedStatement ps = null;
       ResultSet resultSet = null;
       try {
          ps = this.connection.prepareStatement("Select * FROM Customer where name = ? and phone = ?;");
          ps.setString(1, name);
          ps.setLong(2, phone);
          resultSet = ps.executeQuery();
          cust1 = unpackCustomer(resultSet);
       } catch (SQLException e) {
          e.printStackTrace();
       } finally {
          try {
             resultSet.close();
          } catch (SQLException e) {
             e.printStackTrace();
          }
          try {
             ps.close();
          } catch (SQLException e) {
             e.printStackTrace();
          }
       }
       try {
           return (Customer) cust1.toArray()[0];
       }catch (Exception e){
           return null;
       }
    }
}


