package com.hybris.caas.interview.service;

import com.hybris.caas.interview.exceptions.ResourceNotFoundException;
import com.hybris.caas.interview.model.Customer;
import com.hybris.caas.interview.repository.DocumentRepositoryClient;

/**
 * @author SAP Hybris YaaS
 */
public class DefaultCustomerService implements CustomerService
{

    private DocumentRepositoryClient documentClient;

    public DefaultCustomerService(DocumentRepositoryClient documentClient) {
        this.documentClient = documentClient;
    }

    public Customer getCustomerById(String id)
    {
        Customer customer = documentClient.getCustomer(id);
        if (customer == null)
        {
            throw new ResourceNotFoundException("Customer of id '" + id + "' not found.");
        }
        return customer;
    }

    public void updateCustomer(Customer customer)
    {
        documentClient.putCustomer(customer);
    }

}