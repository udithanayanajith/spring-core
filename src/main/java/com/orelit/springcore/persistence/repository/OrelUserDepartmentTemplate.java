package com.orelit.springcore.persistence.repository;

import com.orelit.springcore.persistence.entity.Department;

/**
 * OrelUserDepartmentTemplate
 * Author: udithan
 * Date: 03-Oct-23
 */

/**
 * Defines methods for saving and querying OrelUser Department data.
 */


public interface OrelUserDepartmentTemplate {

    void saveOrelUserDepartmentDetails(Department department);

    void delete(Department department);

    Department findByUserId(Long id);
}
