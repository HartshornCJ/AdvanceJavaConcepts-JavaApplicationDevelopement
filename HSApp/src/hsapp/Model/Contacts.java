/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hsapp.Model;

import hsapp.DAO.Query;
import hsapp.tables.CustomerTB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author christina joy hartshorn
 */
public class Contacts {
    protected ObservableList<Customer> data;
    //protected ArrayList<Part> allParts;
    ResultSet rs;
    CustomerTB customerTB = new CustomerTB();
    
    public Contacts()
    {
        String selectStatement = "select customer.customerId, customer.customerName, customer.active, " +
            "address.addressId, address.address, address.address2, address.postalCode, address.phone, " +
            "city.cityId, city.city, country.countryId, country.country " +
            "From customer, address, city, country " +
            "Where customer.addressId = address.addressId AND address.cityId = city.cityId " +
            "AND city.countryId = country.countryId;";

        try{
            Query.setPrepareStatement(selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            data = FXCollections.observableArrayList();
            while (rs.next()){
                //public Customer(int countryId, String countryName, int cityId, String cityName, int addressId, String address, String address2, String postalCode, String phone, int customerId, String customerName, int active)
            data.add(new Customer(rs.getInt("countryId"), rs.getString("country"), rs.getInt("cityId"), rs.getString("city"), rs.getInt("addressId"), rs.getString("address"),rs.getString("address2"), rs.getString("postalCode"), rs.getString("phone"), rs.getInt("customerId"), rs.getString("customerName"), rs.getInt("active")));
        }

        }
        catch(SQLException e)
        {
            System.out.println(e);
            System.out.println("contracter Contacts");
        } 


    }
    
    public void addCustomer(Customer customer) {
        customer = customerTB.safeInstertCustomer(customer);
        data.add(customer);
    }
    
    public boolean removeCustomer(Customer customer) {
        //return true;
        boolean temp = customerTB.safedeleteCustomer(customer);
        if(temp){
            return data.removeIf(n -> (n.getCustomerId() == customer.getCustomerId()));
        }
        return temp;
    }
    
    public Customer lookupCustomer(int customerId) {
        //return products.get(productID);
        
        for(Customer customer : data) {
            if(customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }
    
    public void updateCustomer(int customerId, Customer updatedCustomer) {
        
        //System.out.println(updatedCustomer.getAddressId() + ", "+ updatedCustomer.getCityId() + ", "+ updatedCustomer.getCountryId());
        updatedCustomer = customerTB.updateCustomer(updatedCustomer);
        //updatedCustomer.setCustomerId(customerId);
        //updatedProduct.correctCount();
        //addCustomer(updatedCustomer);
        data.removeIf(n -> (n.getCustomerId() == customerId));
        data.add(updatedCustomer);
        
    }
    
    public ObservableList<Customer> ObsListCustomer() {
        return data;
    }
}
