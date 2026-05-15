package com.lutz.algashop.billing.application.invoice.query;

import com.lutz.algashop.billing.domain.invoice.PaymentMethod;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSettingsOutput {

    private UUID id;
    private UUID creditCardId;
    private PaymentMethod method;
}
