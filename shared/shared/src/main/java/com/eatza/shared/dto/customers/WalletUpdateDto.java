package com.eatza.shared.dto.customers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletUpdateDto {

    @NotNull(message = "customer.id.is.empty")
    private Long customerId;

    @NotNull(message = "amount.is.empty")
    private Integer amount;

    @NotNull(message = "operation.is.empty")
    private WalletOperation operation;
}
