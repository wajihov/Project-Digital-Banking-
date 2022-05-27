package com.example.digitalbank.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalbank.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

	List<AccountOperation> findBybankAcountsId(String accountId);

	Page<AccountOperation> findBybankAcountsId(String accountId, Pageable pageable);
}
