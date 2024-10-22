package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold customer and account information"
)
public class CustomerDto {
    @NotEmpty(message = "Name can not be null or empty")
    @Size(min= 5, max= 30, message = "The length of the customer should be b/w 5 and 30")
    @Schema(
            description = "Name of customer",
            example = "eazybytes"
    )
    private String name;

    @NotEmpty(message = "Email can not be null or empty")
    @Email(message = "Email address should be a valid value")
    @Schema(
            description = "Email of customer",
            example = "mayank.pal@gmail.com"
    )
    private String email;

    @Pattern(regexp =  "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @Schema(
            description = "Mobile number of customer",
            example = "8892854722"
    )
    private String mobileNumber;

    @Schema(
            description = "Account details of customer"
    )
    private AccountsDto accountsDto;
}
