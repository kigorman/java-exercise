package com.hybris.caas.interview.service;

import com.hybris.caas.interview.exceptions.ResourceNotFoundException;
import com.hybris.caas.interview.model.Address;
import com.hybris.caas.interview.model.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SAP Hybris YaaS
 */
public class DefaultAddressService implements AddressService {

    private CustomerService customerService;

    public DefaultAddressService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public List<Address> getAddressBook(String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        List<Address> addresses = new ArrayList<>(customer.getAddressBook());
        return addresses;
    }

    /**
     * Deletes the address from the customer's address book
     */
    public void deleteAddress(String customerId, String addressId) {
        Customer customer = customerService.getCustomerById(customerId);
        Address address = findAddressInAddressBook(customer.getAddressBook(), addressId);
        customer.getAddressBook().remove(address);
        customerService.updateCustomer(customer);
    }

    /**
     * Finds the address in the customer's address book
     */
    private Address findAddressInAddressBook(List<Address> addressBook, String addressId) {
        final List<Address> filteredAddresses = new ArrayList<>();
        for(final Address a : addressBook){
            if(a.getId().equals(addressId)){
                filteredAddresses.add(a);
            }
        }

        if (filteredAddresses.size() == 1) {
            return filteredAddresses.get(0);
        } else if (filteredAddresses.size() > 1) {
            throw new IllegalStateException("More than one address of if '" + addressId + "' exist.");
        } else {
            throw new ResourceNotFoundException("Address of id '" + addressId + "' not found.");
        }
    }
}
