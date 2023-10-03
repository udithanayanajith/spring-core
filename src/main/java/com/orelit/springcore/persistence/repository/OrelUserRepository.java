package com.orelit.springcore.persistence.repository;


import com.orelit.springcore.persistence.entity.OrelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing OrelUser entities using Spring Data JPA.
 */
@Repository
public interface OrelUserRepository extends JpaRepository<OrelUser, Long> {

  Optional<OrelUser> findByPhoneNo(String phoneNo);

}
