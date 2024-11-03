package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {

    /**
     *
     * @param mobileNumber - Input mobile number
     * @param correlationId - Correlation Id
     * @return Customer details based on a given mobile number
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
