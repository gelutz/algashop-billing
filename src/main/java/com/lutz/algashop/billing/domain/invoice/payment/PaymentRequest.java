package com.lutz.algashop.billing.domain.invoice.payment;

import com.lutz.algashop.billing.domain.invoice.Payer;
import com.lutz.algashop.billing.domain.invoice.PaymentMethod;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
public class PaymentRequest {
	private PaymentMethod method;
	private BigDecimal amount;
	private UUID invoiceId;
	private UUID creditCardId;
	private Payer payer;

	public PaymentRequest(@NonNull PaymentMethod method, @NonNull BigDecimal amount, @NonNull UUID invoiceId, UUID creditCardId, @NonNull Payer payer) {
		if (method.equals(PaymentMethod.CREDIT_CARD)) {
			Objects.requireNonNull(creditCardId);
		}

		this.method = method;
		this.amount = amount;
		this.invoiceId = invoiceId;
		this.creditCardId = creditCardId;
		this.payer = payer;
	}
}
