package com.simon.account.repository;

import com.simon.account.domain.Agreement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Agreement entity.
 */
@SuppressWarnings("unused")
public interface AgreementRepository extends JpaRepository<Agreement,Long> {

}
