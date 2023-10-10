package com.orelit.springcore.persistence.repository.webClient;

import com.orelit.springcore.common.dto.webClient.AccountDetail;
import com.orelit.springcore.common.dto.webClient.AccountDetailResponse;

/**
 * WebClient
 * Author: udithan
 * Date: 10-Oct-23
 */
public interface WebClientTemplate {

    public AccountDetailResponse getAccountDetail(String phoneNo);

    public void saveAccountDetail(AccountDetail accountDetail);

}
