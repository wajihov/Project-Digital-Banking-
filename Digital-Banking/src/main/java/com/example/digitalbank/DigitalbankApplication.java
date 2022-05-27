package com.example.digitalbank;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.digitalbank.DTO.BankAcountDTO;
import com.example.digitalbank.DTO.CurrentBankAcountDTO;
import com.example.digitalbank.DTO.CustomerDTO;
import com.example.digitalbank.DTO.SavingBankAcountDTO;
import com.example.digitalbank.entities.AccountOperation;
import com.example.digitalbank.entities.BankAcount;
import com.example.digitalbank.entities.CurrentAccount;
import com.example.digitalbank.entities.Customer;
import com.example.digitalbank.entities.SavingAccount;
import com.example.digitalbank.enums.AccountStatus;
import com.example.digitalbank.enums.OperationType;
import com.example.digitalbank.exception.BalanceNotSuffisanteException;
import com.example.digitalbank.exception.BankAccountNotFoundException;
import com.example.digitalbank.exception.CustomerNotFoundException;
import com.example.digitalbank.repositories.AccountOperationRepository;
import com.example.digitalbank.repositories.BankAcountRepository;
import com.example.digitalbank.repositories.CustomerRepository;
import com.example.digitalbank.service.BankAcountService;

@SpringBootApplication
public class DigitalbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalbankApplication.class, args);
	}

	@Bean
	CommandLineRunner lineRunner(BankAcountService bankAcountService)
			throws BankAccountNotFoundException, BalanceNotSuffisanteException {
		return args -> {
			Stream.of("hassen", "imed", "imen").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name + "@hotmail.com");
				bankAcountService.saveCustomer(customer);
			});
			bankAcountService.customers().forEach(cust -> {
				try {
					bankAcountService.saveCurrentBankAcount(Math.random() * 12000, 900, cust.getId());
					bankAcountService.saveSavingBankAcount(Math.random() * 1300, 5.5, cust.getId());

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});
			List<BankAcountDTO> bankAcounts = bankAcountService.listAcountBank();
			for (BankAcountDTO bankAcount : bankAcounts) {
				for (int i = 0; i < 10; i++) {
					String acountId;
					if (bankAcount instanceof SavingBankAcountDTO) {
						acountId = ((SavingBankAcountDTO) bankAcount).getId();
					} else {
						acountId = ((CurrentBankAcountDTO) bankAcount).getId();
					}
					bankAcountService.credit(acountId, 12000 + Math.random() * 120000, "Credit");
					bankAcountService.debit(acountId, 10000 + Math.random() * 10000, "Debit");
				}
			}
		};
	}

	// 2/ @Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
			BankAcountRepository bankAcountRepository, AccountOperationRepository accountOperationRepository) {
		return args -> {
			BankAcount bankAcount = bankAcountRepository.findById("29336f3f-40b5-437f-800e-cedf98fb5dc8").orElse(null);

			System.out.println("la compte est : " + bankAcount.getId() + " \t" + bankAcount.getCreatedAt() + " \t"
					+ bankAcount.getBalance() + " \t" + bankAcount.getCurency());
			System.out.println(" le nom de la classe est : " + bankAcount.getClass().getName());
			if (bankAcount instanceof CurrentAccount)
				System.out.println("Current Account : " + ((CurrentAccount) bankAcount).getOverDraft());
			else if (bankAcount instanceof SavingAccount)
				System.out.println("Saving Acount : " + ((SavingAccount) bankAcount).getInterestRate());

			/*
			 * bankAcount.getAccountOperations().forEach(op -> { System.out.println("Op " +
			 * op.getAmount() + " \t" + op.getBankAcounts() + " \t" + op.getDate() + " \t" +
			 * op.getType()); });
			 */

		};
	}

	// 1/ @Bean
	CommandLineRunner start(CustomerRepository customerRepository, BankAcountRepository bankAcountRepository,
			AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("wajih", "yassine", "amine", "hamza", "souheil").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gopartner.fr");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(90000);
				currentAccount.setStatus(AccountStatus.CREATED);
				bankAcountRepository.save(currentAccount);
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(5.5);
				savingAccount.setStatus(AccountStatus.CREATED);
				bankAcountRepository.save(savingAccount);
			});
			bankAcountRepository.findAll().forEach(acc -> {
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setDate(new Date());
					accountOperation.setAmount(Math.random() * 120000);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.CREADIT : OperationType.DEBIT);
					accountOperation.setBankAcounts(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}

}
