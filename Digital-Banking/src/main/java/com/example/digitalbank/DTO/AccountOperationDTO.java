package com.example.digitalbank.DTO;

import java.util.Date;

import com.example.digitalbank.enums.OperationType;

import lombok.Data;

@Data
public class AccountOperationDTO {

	private Long id;
	private Date date;
	private double amount;
	private String description;
	private OperationType type;

}
