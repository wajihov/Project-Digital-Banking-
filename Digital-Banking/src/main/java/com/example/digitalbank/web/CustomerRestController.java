package com.example.digitalbank.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digitalbank.DTO.CustomerDTO;
import com.example.digitalbank.entities.Customer;
import com.example.digitalbank.exception.CustomerNotFoundException;
import com.example.digitalbank.service.BankAcountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("http://localhost:4200")
public class CustomerRestController {

	private BankAcountService bankAcountService;

	@GetMapping("/customers")
	public List<Customer> customers() {
		return bankAcountService.customers();
	}

	@GetMapping("/customers/search")
	public List<CustomerDTO> SearchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		return bankAcountService.searchCustomers(keyword);
	}

	@GetMapping("/customers/cherche")
	public List<CustomerDTO> chercheClients() {
		return bankAcountService.listCustomerDto();
	}
 
	@GetMapping("/customerDtos")
	public List<CustomerDTO> customerDTOs() {
		return bankAcountService.listCustomerDto();
	}

	@GetMapping("/customers/{id}")
	public CustomerDTO getCustomer(@PathVariable(name = "id") Long idCustomer) throws CustomerNotFoundException {
		return bankAcountService.getCustomer(idCustomer);
	}

	@PostMapping("/customers") 
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO request) {
		CustomerDTO customer = bankAcountService.saveCustomer(request);
		return customer;
	}

	@PutMapping(value = "/customers/{id}")
	public CustomerDTO updateCustomer(@RequestBody CustomerDTO request, @PathVariable(name = "id") Long id)
			throws CustomerNotFoundException {
		return bankAcountService.miseAjourCustomer(id, request);
	}

	@DeleteMapping("/customers/{id}")
	public void romoveCustomer(@PathVariable Long id) {
		bankAcountService.deleteCustomer(id);
	}

}
