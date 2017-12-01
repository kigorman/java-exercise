package com.hybris.caas.interview.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.hybris.caas.interview.exceptions.ResourceNotFoundException;
import com.hybris.caas.interview.model.Address;
import com.hybris.caas.interview.model.Customer;

/**
 * @author khalid.igorman
 */

public class DefaultAddressServiceTest {

	private AddressService addressService;
	private CustomerService customerService;
	private Customer customerWithoutAddress;
	private Customer customerWithAddress;
	private Customer unkownCustomer;
	private Address address;

	@Before
	public void setup() {
		
		customerService = Mockito.mock(DefaultCustomerService.class);
		addressService = new DefaultAddressService(customerService);
		unkownCustomer = createUnkownCustomer();
		customerWithoutAddress = createCustomerWithtoutAddress();
		customerWithAddress = createCustomerWithtAddress();
	}
	
	@Test
	public void testGetAddressBook() {
		when(customerService.getCustomerById(customerWithoutAddress.getId())).thenReturn(customerWithoutAddress);
		Assert.assertTrue("Address collection not empty for a homeless.",
				this.addressService.getAddressBook(customerWithoutAddress.getId()).isEmpty());
		when(customerService.getCustomerById(customerWithAddress.getId())).thenReturn(customerWithAddress);
		List<Address> addresses = this.addressService.getAddressBook(customerWithAddress.getId());
		Assert.assertEquals("Address collection size differs.", 1L, (long) addresses.size());
		Assert.assertEquals("Address not found in collection.", address, addresses.iterator().next());
	}

	@Test(expected = ResourceNotFoundException.class)
	public void testFailBehavior() {
		when(customerService.getCustomerById(unkownCustomer.getId())).thenThrow(new ResourceNotFoundException());
		addressService.getAddressBook(unkownCustomer.getId());
	}

	@Test
	public void testDeleteAddress() {
		when(customerService.getCustomerById(customerWithAddress.getId())).thenReturn(customerWithAddress);
		List<Address> addresses = this.addressService.getAddressBook(customerWithAddress.getId());
		Assert.assertEquals("Address collection size differs.", 1L, (long) addresses.size());
		addressService.deleteAddress(customerWithAddress.getId(), address.getId());
		Mockito.verify(customerService).deleteAddress(customerWithAddress, address);

	}

	private Customer createUnkownCustomer() {
		Customer unkownCustomer = new Customer();
		unkownCustomer.setId("mr.unknown@gmx.de");
		return unkownCustomer;
	}
	
	private Customer createCustomerWithtoutAddress() {
		Customer customer = new Customer();
		customer.setId("mr.homeless@gmx.de");
		customer.setAddressBook(new ArrayList<Address>());
		return customer;
	}

	private Customer createCustomerWithtAddress() {
		address = new Address();
		address.setId("00001");
		address.setStreet("Hamburger str.");
		address.setStreetNumber("12");
		address.setCity("Hamburg");
		address.setZipCode("22308");
		address.setCountry("Germany");

		Customer customer = new Customer();
		customer.setId("mr.chef@gmx.de");
		customer.setAddressBook(Collections.singletonList(address));

		return customer;

	}

}