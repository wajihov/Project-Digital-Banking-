package com.example.digitalbank.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AccountHistoryDTO {

	private String acountId;
	private double balance;
	private String accountType;
	private int currentPage;
	private int totalPages;
	private int pageSize;
	private List<AccountOperationDTO> accountOperationDTOs;

}
