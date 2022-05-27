package com.example.digitalbank.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.digitalbank.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING)
public abstract class BankAcount {

	@Id
	private String id;
	private Date createdAt;
	private double balance;
	private String curency;
	@ManyToOne
	// @JoinColumn(name = "cust_id")
	private Customer customer;
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	@OneToMany(mappedBy = "bankAcounts", fetch = FetchType.LAZY)
	private List<AccountOperation> accountOperations;
}
