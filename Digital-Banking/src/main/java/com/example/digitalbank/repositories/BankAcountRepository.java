package com.example.digitalbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalbank.entities.BankAcount;

public interface BankAcountRepository extends JpaRepository<BankAcount, String> {

}
