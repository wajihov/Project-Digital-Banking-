package com.example.digitalbank.Mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.digitalbank.DTO.AccountOperationDTO;
import com.example.digitalbank.DTO.CurrentBankAcountDTO;
import com.example.digitalbank.DTO.CustomerDTO;
import com.example.digitalbank.DTO.SavingBankAcountDTO;
import com.example.digitalbank.entities.AccountOperation;
import com.example.digitalbank.entities.CurrentAccount;
import com.example.digitalbank.entities.Customer;
import com.example.digitalbank.entities.SavingAccount;

@Service
public class BankAcountMapperImpl {

	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);
		// customerDTO.setId(customer.getId());
		// customerDTO.setName(customer.getName());
		// customerDTO.setEmail(customer.getEmail());
		return customerDTO;
	}

	public Customer fromCustomerDTO(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}

	public SavingBankAcountDTO fromSavingBankAcountDTO(SavingAccount savingAccount) {
		SavingBankAcountDTO savingBankAcountDTO = new SavingBankAcountDTO();
		BeanUtils.copyProperties(savingAccount, savingBankAcountDTO);
		savingBankAcountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
		savingBankAcountDTO.setType(savingAccount.getClass().getSimpleName());
		return savingBankAcountDTO;
	}

	public SavingAccount fromSavingBankAcoun(SavingBankAcountDTO savingBankAcountDTO) {
		SavingAccount savingAccount = new SavingAccount();
		BeanUtils.copyProperties(savingBankAcountDTO, savingAccount);
		savingAccount.setCustomer(fromCustomerDTO(savingBankAcountDTO.getCustomerDTO()));
		return savingAccount;
	}

	public CurrentBankAcountDTO fromCurrentBankAcountDTO(CurrentAccount currentAccount) {
		CurrentBankAcountDTO currentBankAcountDTO = new CurrentBankAcountDTO();
		BeanUtils.copyProperties(currentAccount, currentBankAcountDTO);
		currentBankAcountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
		currentBankAcountDTO.setType(currentAccount.getClass().getSimpleName());
		return currentBankAcountDTO;
	}

	public CurrentAccount fromCurrentAccount(CurrentBankAcountDTO currentBankAcountDTO) {
		CurrentAccount currentAccount = new CurrentAccount();
		BeanUtils.copyProperties(currentBankAcountDTO, currentAccount);
		currentAccount.setCustomer(fromCustomerDTO(currentBankAcountDTO.getCustomerDTO()));
		return currentAccount;
	}

	public AccountOperation fromAccountOperation(AccountOperationDTO accountOperationDTO) {
		AccountOperation accountOperation = new AccountOperation();
		BeanUtils.copyProperties(accountOperationDTO, accountOperation);
		return accountOperation;
	}

	public AccountOperationDTO fromAccountOperationDTO(AccountOperation accountOperation) {
		AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
		BeanUtils.copyProperties(accountOperation, accountOperationDTO);
		return accountOperationDTO;
	}

}
