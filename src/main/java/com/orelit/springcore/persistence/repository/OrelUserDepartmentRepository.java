package com.orelit.springcore.persistence.repository;

import com.orelit.springcore.persistence.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * OrelUserDepartmentRepository
 * Author: udithan
 * Date: 03-Oct-23
 */

/**
 * Repository interface for managing OrelUser Department entities using Spring Data JPA.
 */

@Repository
public interface OrelUserDepartmentRepository extends JpaRepository<Department, Long> {

        Optional<Department> findByOrelUserId(Long userId);
}
