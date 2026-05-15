package com.lutz.algashop.billing.application.invoice.query;

import com.lutz.algashop.billing.application.invoice.PayerData;
import com.lutz.algashop.billing.domain.invoice.InvoiceStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceOutput {

    private UUID id;
    private String orderId;
    private UUID customerId;
    private OffsetDateTime issuedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime expiresAt;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private PayerData payerData;
    private PaymentSettingsOutput paymentSettings;
}
