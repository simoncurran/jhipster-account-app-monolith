package com.simon.account.repository;

import com.simon.account.domain.Broker;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Broker entity.
 */
@SuppressWarnings("unused")
public interface BrokerRepository extends JpaRepository<Broker,Long> {

}
