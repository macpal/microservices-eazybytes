package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
public class ErrorResponseDto {
    @Schema(description = "API path invoked by client")
    private String apiPath;
    @Schema(description = "Error code representing error happened")
    private HttpStatus errorCode;
    @Schema(description = "Error msg representing error happened")
    private String errorMessage;
    private LocalDateTime errorTime;
}