package com.example.digitalbank.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digitalbank.DTO.AccountHistoryDTO;
import com.example.digitalbank.DTO.AccountOperationDTO;
import com.example.digitalbank.DTO.BankAcountDTO;
import com.example.digitalbank.exception.BankAccountNotFoundException;
import com.example.digitalbank.service.BankAcountService;

@RestController
@CrossOrigin("*")
public class BankAcountRestAPI {

	private BankAcountService bankAcountService;

	public BankAcountRestAPI(BankAcountService bankAcountService) {
		super();
		this.bankAcountService = bankAcountService;
	}

	@GetMapping("/bankAcounts/{acountId}")
	public BankAcountDTO getBankAcount(@PathVariable(name = "acountId") String acountId)
			throws BankAccountNotFoundException {
		return bankAcountService.getBankAcount(acountId);
	}

	@GetMapping("/bankAcounts")
	public List<BankAcountDTO> getBankAcounts() throws BankAccountNotFoundException {
		return bankAcountService.listAcountBank();
	}

	@GetMapping("/bankAcountsOperations/{accountId}")
	public List<AccountOperationDTO> listOperations(@PathVariable String accountId) {
		return bankAcountService.accountHistorique(accountId);
	}

	@GetMapping("/bankAcounts/{accountId}/operations")
	public List<AccountOperationDTO> getOperations(@PathVariable String accountId) {
		return bankAcountService.accountHistorique(accountId);
	}

	@GetMapping("/bankAcounts/{accountId}/pageOperations")
	public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
		return bankAcountService.getAccountHistory(accountId, page, size);
	}

}
