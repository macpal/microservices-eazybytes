package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.*;
import com.eazybytes.accounts.service.IAccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(
        name = "CRUD REST APIs for Accounts in EasyBank",
        description = "CRUD REST APIs to CREATE, DELETE, UPDATE, FETCH for Account Details"
)
public class AccountsController {

    private final IAccountsService iAccountsService;
    private final AccountsContactInfoDto accountsContactInfoDto;
    private final Logger logger = LoggerFactory.getLogger(AccountsController.class);
    private final Environment environment;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    public AccountsController(IAccountsService iAccountsService, AccountsContactInfoDto accountsContactInfoDto, Environment environment) {
        this.iAccountsService = iAccountsService;
        this.accountsContactInfoDto = accountsContactInfoDto;
        this.environment = environment;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new customer and account inside eazybank"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Account REST API",
            description = "REST API to fetch customer and account details based on mobile number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping(path = "/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                               @Pattern(regexp =  "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                               String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(customerDto);
    }

    @PutMapping("/update")
    @Operation(
            summary = "Update Account REST API",
            description = "REST API to update a customer and account inside eazybank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content (
                            schema = @Schema(implementation =  ErrorResponseDto.class)
                    )
            )
    }
    )
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete Account REST API",
            description = "REST API to delete a customer and account inside eazybank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                                @Pattern(regexp =  "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("JAVA 17");
    }

        @Operation(
            summary = "Get build information",
            description = "Get build information for eazybank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @GetMapping("/build-info")
    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    public ResponseEntity<String> getBuildInfo() throws TimeoutException {
        logger.info("getBuildInfo() method Invoked");
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(buildVersion);
    }

    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
        logger.info("getBuildInfoFallback() method Invoked");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("0.9");
    }

    @Operation(
            summary = "Get contact information",
            description = "Get contact information for eazybank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        logger.info("Inside getContactInfo()");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }
}
