package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to hold Account information"
)
public class AccountsDto {

    @NotEmpty(message = "Account number can not be null or empty")
    @Pattern(regexp =  "(^$|[0-9]{10})", message = "Account number must be 10 digits")
    @Schema(
            description = "account of customer"
    )
    private Long accountNumber;

    @NotEmpty(message = "Account type can not be null or empty")
    @Schema(
            description = "Account type of customer",
            example = "Savings"
    )
    private String accountType;

    @NotEmpty(message = "Branch address can not be null or empty")
    @Schema(
            description = "Branch address of customer"
    )
    private String branchAddress;
}
