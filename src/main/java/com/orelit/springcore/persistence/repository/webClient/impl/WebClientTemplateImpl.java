package com.orelit.springcore.persistence.repository.webClient.impl;

import com.orelit.springcore.common.dto.webClient.AccountDetail;
import com.orelit.springcore.common.dto.webClient.AccountDetailResponse;
import com.orelit.springcore.persistence.repository.webClient.WebClientTemplate;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static com.orelit.springcore.common.constant.WebClientErrorMessageConstants.*;
import static com.orelit.springcore.common.constant.webClientConstants.*;

/**
 * WebClientImpl
 * Author: udithan
 * Date: 10-Oct-23
 */
@Component
public class WebClientTemplateImpl implements WebClientTemplate {

    private static final Logger logger = LoggerFactory.getLogger(WebClientTemplate.class);
    @Value("${api.base-url}")
    private String baseUrl;

    @Override
    public AccountDetailResponse getAccountDetail(String phoneNo) {

        WebClient webClient = WebClient.builder().baseUrl(baseUrl + bank_service).build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GET_ACCOUNT_DETAILS_V1).queryParam(phone_no, phoneNo)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> {
                            logger.error(API_NOT_FOUND);
                            return Mono.error(new RuntimeException(API_NOT_FOUND));
                        })
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> {
                            logger.error(SEVER_IS_NOT_RESPONDING);
                            return Mono.error(new RuntimeException(SEVER_IS_NOT_RESPONDING));
                        })
                .bodyToMono(AccountDetailResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .doOnSuccess(response -> logger.info(SUCCESSFULLY_RETRIEVED))
                .doOnError(error -> logger.error(FAILED, error))
                .block();

    }

    @Override
    public void saveAccountDetail(AccountDetail accountDetail) {

        WebClient webClient = WebClient.builder().baseUrl(baseUrl + bank_service).build();
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(SAVE_ACCOUNT_DETAIL_V1).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(accountDetail)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> {
                            logger.error(API_NOT_FOUND);
                            return Mono.error(new RuntimeException(API_NOT_FOUND));
                        })
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> {
                            logger.error(SEVER_IS_NOT_RESPONDING);
                            return Mono.error(new RuntimeException(SEVER_IS_NOT_RESPONDING));
                        })

                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .doOnSuccess(response -> logger.info(SUCCESSFULLY_RETRIEVED))
                .doOnError(error -> logger.error(FAILED, error))
                .block();
    }

}
