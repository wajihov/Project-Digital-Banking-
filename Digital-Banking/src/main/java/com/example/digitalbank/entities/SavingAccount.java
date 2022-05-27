package com.example.digitalbank.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DiscriminatorValue("SA")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccount extends BankAcount {
	private double interestRate;
}
