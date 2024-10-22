package com.eazybytes.loans.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "loans")
@Getter @Setter
public class LoansDetailsInfoDto {

    private String message;
    private Map<String, String> contactDetails;
    private String onCallSupport;
}
