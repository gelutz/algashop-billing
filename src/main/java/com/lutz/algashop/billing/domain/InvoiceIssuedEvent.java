package com.lutz.algashop.billing.domain;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvoiceIssuedEvent {

    private UUID invoiceId;
    private UUID customerId;
    private String orderId;
    private OffsetDateTime issuedAt;
}
