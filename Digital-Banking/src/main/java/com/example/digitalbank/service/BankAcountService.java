package com.example.digitalbank.service;

import java.util.List;

import com.example.digitalbank.DTO.AccountHistoryDTO;
import com.example.digitalbank.DTO.AccountOperationDTO;
import com.example.digitalbank.DTO.BankAcountDTO;
import com.example.digitalbank.DTO.CurrentBankAcountDTO;
import com.example.digitalbank.DTO.CustomerDTO;
import com.example.digitalbank.DTO.SavingBankAcountDTO;
import com.example.digitalbank.entities.Customer;
import com.example.digitalbank.exception.BalanceNotSuffisanteException;
import com.example.digitalbank.exception.BankAccountNotFoundException;
import com.example.digitalbank.exception.CustomerNotFoundException;

public interface BankAcountService {

	// Customer saveCustomer(Customer customer);
	CustomerDTO saveCustomer(CustomerDTO customerDTO);

	CurrentBankAcountDTO saveCurrentBankAcount(double initBalance, double overDraft, Long customerID)
			throws CustomerNotFoundException;

	SavingBankAcountDTO saveSavingBankAcount(double initBalance, double interestRate, Long customerID)
			throws CustomerNotFoundException;

	List<Customer> customers();

	List<CustomerDTO> listCustomerDto();

	BankAcountDTO getBankAcount(String acountID) throws BankAccountNotFoundException;

	void debit(String acountId, double amount, String decription)
			throws BankAccountNotFoundException, BalanceNotSuffisanteException;

	void credit(String acountId, double amount, String decription) throws BankAccountNotFoundException;

	void transfert(String acountIdSource, String acountIdDistination, double montant)
			throws BankAccountNotFoundException, BalanceNotSuffisanteException;

	List<BankAcountDTO> listAcountBank();

	CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

	CustomerDTO miseAjourCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException;

	void deleteCustomer(Long id);
	
	List<CustomerDTO> searchCustomers(String keyword);
	

	List<AccountOperationDTO> accountHistorique(String accountId);

	AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

}
