package com.orelit.springcore.persistence.repository.impl;


import com.orelit.springcore.persistence.converter.OrelUserMapper;
import com.orelit.springcore.persistence.entity.OrelUser;
import com.orelit.springcore.persistence.repository.OrelUserRepository;
import com.orelit.springcore.persistence.repository.OrelUserTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the OrelUserTemplate interface that provides methods for saving and querying OrelUser entities.
 */
@Service
public class OrelUserTemplateImpl implements OrelUserTemplate {


    public final OrelUserRepository orelUserRepository;

    private final OrelUserMapper orelUserMapper;

    @Autowired
    public OrelUserTemplateImpl(OrelUserRepository orelUserRepository,
                                OrelUserMapper orelUserMapper) {

        this.orelUserRepository = orelUserRepository;
        this.orelUserMapper = orelUserMapper;

    }

    /**
     * Saves a orelUser entity.
     *
     * @param orelUser The orelUser entity to be saved.
     * @return The saved orelUser entity.
     */
    @Override
    public OrelUser save(OrelUser orelUser) {

        return orelUserRepository.save(orelUser);
    }

    /**
     * Finds a OrelUser entity by its phone number.
     *
     * @param phoneNo The phone number of the OrelUser to find.
     * @return The OrelUser entity if found, or null if not found.
     */
    @Override
    public OrelUser findByPhoneNo(String phoneNo) {

        Optional<OrelUser> userOptional = orelUserRepository.findByPhoneNo(phoneNo);
        return userOptional.orElse(null);
    }

    /**
     * Find By user ID(Long)
     *
     * @param id - Long id
     * @return
     */

    @Override
    public OrelUser findById(Long id) {

        return orelUserRepository.findById(id).get();
    }

    /**
     * Get all users with department details.
     *
     * @return
     */
    @Override
    public List<OrelUser> findAll() {

        return orelUserRepository.findAll();
    }

    /**
     * Delete user
     *
     * @param orelUser- contains details of the user
     */
    @Override
    public void delete(OrelUser orelUser) {

        orelUserRepository.delete(orelUser);
    }
}
