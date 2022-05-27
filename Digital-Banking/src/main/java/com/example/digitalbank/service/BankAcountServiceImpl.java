package com.example.digitalbank.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.digitalbank.DTO.AccountHistoryDTO;
import com.example.digitalbank.DTO.AccountOperationDTO;
import com.example.digitalbank.DTO.BankAcountDTO;
import com.example.digitalbank.DTO.CurrentBankAcountDTO;
import com.example.digitalbank.DTO.CustomerDTO;
import com.example.digitalbank.DTO.SavingBankAcountDTO;
import com.example.digitalbank.Mapper.BankAcountMapperImpl;
import com.example.digitalbank.entities.AccountOperation;
import com.example.digitalbank.entities.BankAcount;
import com.example.digitalbank.entities.CurrentAccount;
import com.example.digitalbank.entities.Customer;
import com.example.digitalbank.entities.SavingAccount;
import com.example.digitalbank.enums.OperationType;
import com.example.digitalbank.exception.BalanceNotSuffisanteException;
import com.example.digitalbank.exception.BankAccountNotFoundException;
import com.example.digitalbank.exception.CustomerNotFoundException;
import com.example.digitalbank.repositories.AccountOperationRepository;
import com.example.digitalbank.repositories.BankAcountRepository;
import com.example.digitalbank.repositories.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAcountServiceImpl implements BankAcountService {

	private AccountOperationRepository accountOperationRepository;
	private BankAcountRepository bankAcountRepository;
	private CustomerRepository customerRepository;
	private BankAcountMapperImpl mapperDTO;

	@Override
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
		log.info("saving Customer");
		Customer customer = mapperDTO.fromCustomerDTO(customerDTO);
		Customer savedcustomer = customerRepository.save(customer);
		return mapperDTO.fromCustomer(savedcustomer);
	}

	@Override
	public CurrentBankAcountDTO saveCurrentBankAcount(double initBalance, double overDraft, Long customerID)
			throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerID).orElse(null);
		if (customer == null)
			throw new CustomerNotFoundException("Customer Not found");
		CurrentAccount currentAccount = new CurrentAccount();
		currentAccount.setId(UUID.randomUUID().toString());
		currentAccount.setCreatedAt(new Date());
		currentAccount.setBalance(initBalance);
		currentAccount.setCustomer(customer);
		currentAccount.setOverDraft(overDraft);
		CurrentAccount savedCurrentAccount = bankAcountRepository.save(currentAccount);
		return mapperDTO.fromCurrentBankAcountDTO(savedCurrentAccount);

	}

	@Override
	public SavingBankAcountDTO saveSavingBankAcount(double initBalance, double interestRate, Long customerID)
			throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerID).orElse(null);
		if (customer == null)
			throw new CustomerNotFoundException("Customer Not found");
		SavingAccount savingAccount = new SavingAccount();
		savingAccount.setId(UUID.randomUUID().toString());
		savingAccount.setCreatedAt(new Date());
		savingAccount.setBalance(initBalance);
		savingAccount.setCustomer(customer);
		savingAccount.setInterestRate(interestRate);
		SavingAccount savedSavingAccount = bankAcountRepository.save(savingAccount);
		return mapperDTO.fromSavingBankAcountDTO(savedSavingAccount);
	}

	@Override
	public List<Customer> customers() {
		return customerRepository.findAll();
	}

	@Override
	public List<CustomerDTO> listCustomerDto() {
		List<Customer> customers = customerRepository.findAll();
		List<CustomerDTO> customerDTOs = customers.stream().map(cust -> mapperDTO.fromCustomer(cust))
				.collect(Collectors.toList());

		/*
		 * List<CustomerDTO> customerDTOs = new ArrayList<>(); for (Customer customer :
		 * customers) { CustomerDTO customerDTO = mapperDTO.fromCustomer(customer);
		 * customerDTOs.add(customerDTO); } return customerDTOs;
		 */
		return customerDTOs;
	}

	@Override
	public BankAcountDTO getBankAcount(String acountID) throws BankAccountNotFoundException {
		BankAcount bankAcount = bankAcountRepository.findById(acountID)
				.orElseThrow(() -> new BankAccountNotFoundException("BankAcount Not  Found "));

		if (bankAcount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) bankAcount;
			return mapperDTO.fromSavingBankAcountDTO(savingAccount);
		} else {
			CurrentAccount currentAccount = (CurrentAccount) bankAcount;
			return mapperDTO.fromCurrentBankAcountDTO(currentAccount);
		}

	}

	@Override
	public void debit(String acountId, double amount, String decription)
			throws BankAccountNotFoundException, BalanceNotSuffisanteException {

		BankAcount bankAcount = bankAcountRepository.findById(acountId)
				.orElseThrow(() -> new BankAccountNotFoundException("BankAcount Not  Found "));
		if (bankAcount.getBalance() < amount)
			throw new BalanceNotSuffisanteException("le montant n'est pas suffisant");
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(decription);
		accountOperation.setDate(new Date());
		accountOperation.setBankAcounts(bankAcount);
		accountOperationRepository.save(accountOperation);
		bankAcount.setBalance(bankAcount.getBalance() - amount);
		bankAcountRepository.save(bankAcount);

	}

	@Override
	public void credit(String acountId, double amount, String decription) throws BankAccountNotFoundException {
		BankAcount bankAcount = bankAcountRepository.findById(acountId)
				.orElseThrow(() -> new BankAccountNotFoundException("BankAcount Not  Found "));

		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.CREADIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(decription);
		accountOperation.setDate(new Date());
		accountOperation.setBankAcounts(bankAcount);
		accountOperationRepository.save(accountOperation);
		bankAcount.setBalance(bankAcount.getBalance() + amount);
		bankAcountRepository.save(bankAcount);
	}

	@Override
	public void transfert(String acountIdSource, String acountIdDistination, double montant)
			throws BankAccountNotFoundException, BalanceNotSuffisanteException {
		debit(acountIdSource, montant, "Transfer to " + acountIdDistination);
		credit(acountIdDistination, montant, "Transfer from " + acountIdSource);
	}

	/*
	 * if (bankAcounts instanceof SavingAccount) { SavingAccount savingAccount =
	 * (SavingAccount) bankAcounts; return (List<BankAcountDTO>)
	 * mapperDTO.fromSavingBankAcountDTO(savingAccount); } else { CurrentAccount
	 * currentAccount = (CurrentAccount) bankAcounts; return (List<BankAcountDTO>)
	 * mapperDTO.fromCurrentBankAcountDTO(currentAccount); }
	 */
	@Override
	public List<BankAcountDTO> listAcountBank() {
		List<BankAcount> bankAcounts = bankAcountRepository.findAll();
		List<BankAcountDTO> bankAcountDTOs = bankAcounts.stream().map(bankAcount -> {

			if (bankAcount instanceof SavingAccount) {
				SavingAccount savingAccount = (SavingAccount) bankAcount;
				return mapperDTO.fromSavingBankAcountDTO(savingAccount);
			} else {
				CurrentAccount currentAccount = (CurrentAccount) bankAcount;
				return mapperDTO.fromCurrentBankAcountDTO(currentAccount);
			}
		}).collect(Collectors.toList());
		return bankAcountDTOs;
	}

	@Override
	public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
		return mapperDTO.fromCustomer(customer);

	}

	@Override
	public CustomerDTO miseAjourCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException {

		log.info("updating Customer");
		CustomerDTO customerDTOCurrently = getCustomer(id);
		String nameDTo = customerDTO.getName();
		if (nameDTo != null) {
			customerDTOCurrently.setName(nameDTo);
		}
		String emailDTO = customerDTO.getEmail();
		if (emailDTO != null) {
			customerDTOCurrently.setEmail(emailDTO);
		}
		Customer customerUpdate = mapperDTO.fromCustomerDTO(customerDTOCurrently);
		Customer savedcustomer = customerRepository.save(customerUpdate);
		return mapperDTO.fromCustomer(savedcustomer);

	}

	@Override
	public void deleteCustomer(Long id) {
		/*
		 * log.info("updating Customer"); CustomerDTO customerDTO = getCustomer(id);
		 * Customer customer = mapperDTO.fromCustomerDTO(customerDTO);
		 * customerRepository.delete(customer);
		 */
		log.info("le id est  info : " + id);
		System.out.println("le id est SOUT : " + id);
		customerRepository.deleteById(id);
	}

	@Override
	public List<AccountOperationDTO> accountHistorique(String accountId) {
		List<AccountOperation> accountOperations = accountOperationRepository.findBybankAcountsId(accountId);
		return accountOperations.stream().map(op -> mapperDTO.fromAccountOperationDTO(op)).collect(Collectors.toList());
	}

	@Override
	public AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
			throws BankAccountNotFoundException {
		BankAcount bankAcount = bankAcountRepository.findById(accountId).orElse(null);
		if (bankAcount == null)
			throw new BankAccountNotFoundException("Account not found");
		Page<AccountOperation> acountOperations = accountOperationRepository.findBybankAcountsId(accountId,
				PageRequest.of(page, size));
		AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
		List<AccountOperationDTO> accountOperationDTOs = acountOperations.getContent().stream()
				.map(op -> mapperDTO.fromAccountOperationDTO(op)).collect(Collectors.toList());
		accountHistoryDTO.setAccountOperationDTOs(accountOperationDTOs);
		accountHistoryDTO.setAcountId(bankAcount.getId());
		accountHistoryDTO.setBalance(bankAcount.getBalance());
		accountHistoryDTO.setCurrentPage(page);
		accountHistoryDTO.setPageSize(size);
		accountHistoryDTO.setTotalPages(acountOperations.getTotalPages());
		accountHistoryDTO.setAccountType(bankAcount.getClass().getSimpleName());
		return accountHistoryDTO;
	}

	@Override
	public List<CustomerDTO> searchCustomers(String keyword) {
		List<Customer> customers = customerRepository.searchByName("%" + keyword +  "%");
		List<CustomerDTO> listCustomers = customers.stream().map(cust -> mapperDTO.fromCustomer(cust))
				.collect(Collectors.toList());
		return listCustomers;
	}

}
