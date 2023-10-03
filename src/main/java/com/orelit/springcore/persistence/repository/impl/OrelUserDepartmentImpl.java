package com.orelit.springcore.persistence.repository.impl;

import com.orelit.springcore.persistence.entity.Department;
import com.orelit.springcore.persistence.repository.OrelUserDepartmentRepository;
import com.orelit.springcore.persistence.repository.OrelUserDepartmentTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * OrelUserDepartmentImpl
 * Author: udithan
 * Date: 03-Oct-23
 */

/**
 * Implementation of the OrelUserDepartmentTemplate interface that provides methods for saving and querying OrelUser and department entities.
 */
@Service
public class OrelUserDepartmentImpl implements OrelUserDepartmentTemplate {

    private OrelUserDepartmentRepository orelUserDepartmentRepository;

    public OrelUserDepartmentImpl(OrelUserDepartmentRepository orelUserDepartmentRepository) {

        this.orelUserDepartmentRepository = orelUserDepartmentRepository;
    }


    /**
     * Save orel user department data.
     *
     * @param department - Contains department data.
     */

    @Override
    public void saveOrelUserDepartmentDetails(Department department) {

        orelUserDepartmentRepository.save(department);
    }

    /**
     * Delete department data.
     *
     * @param department -Contains department data.
     */

    @Override
    public void delete(Department department) {
        orelUserDepartmentRepository.delete(department);
    }

    /**
     * Get saved department data to delete
     *
     * @param userId - Find department which related to the founded user by phone no.
     * @return
     */

    @Override
    public Department findByUserId(Long userId) {

        Optional<Department> departmentOptional = orelUserDepartmentRepository.findByOrelUserId(userId);
        return departmentOptional.orElse(null);
    }
}
