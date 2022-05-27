package com.example.digitalbank.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.digitalbank.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByNameContains(String keyword);

	@Query("Select c from Customer c where c.name like :kw")
	List<Customer> searchByName(@Param(value = "kw") String name);

}
