package com.orelit.springcore.persistence.repository;


import com.orelit.springcore.persistence.entity.OrelUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Defines methods for saving and querying OrelUser data.
 */

public interface OrelUserTemplate {

    OrelUser save(OrelUser orelUser);

    OrelUser findByPhoneNo(String phoneNo);

    OrelUser findById(Long id);

    List<OrelUser> findAll();

    void delete(OrelUser orelUser);
}



