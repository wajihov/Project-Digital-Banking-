package com.example.digitalbank.DTO;

import java.util.Date;

import com.example.digitalbank.enums.AccountStatus;

import lombok.Data;

@Data
public class SavingBankAcountDTO extends BankAcountDTO {

	private String id;
	private Date createdAt;
	private double balance;
	private String curency;
	private AccountStatus status;
	private double interestRate;

	private CustomerDTO customerDTO;

}
