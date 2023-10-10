package com.orelit.springcore.persistence.repository;

import com.orelit.springcore.common.dto.AccountDetailsResponseDto;

/**
 * WebClient
 * Author: udithan
 * Date: 10-Oct-23
 */
public interface WebClient {

    public AccountDetailsResponseDto getAccountDetails(String phoneNo);

}
