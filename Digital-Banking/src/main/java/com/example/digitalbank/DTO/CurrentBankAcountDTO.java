package com.example.digitalbank.DTO;

import java.util.Date;
import com.example.digitalbank.enums.AccountStatus;
import lombok.Data;

@Data
public class CurrentBankAcountDTO extends BankAcountDTO {

	private String id;
	private Date createdAt;
	private double balance;
	private String curency;
	private AccountStatus status;
	private CustomerDTO customerDTO;

	private double overDraft;

}
