package com.tpt.tpt_ecom.repository;

import com.tpt.tpt_ecom.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
