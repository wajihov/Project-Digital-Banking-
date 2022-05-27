package com.example.digitalbank.exception;

public class BalanceNotSuffisanteException extends Exception {

	private static final long serialVersionUID = 1L;

	public BalanceNotSuffisanteException(String message) {
		super(message);
	}

}
