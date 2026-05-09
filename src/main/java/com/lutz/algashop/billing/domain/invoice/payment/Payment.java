package com.lutz.algashop.billing.domain.invoice.payment;

import com.lutz.algashop.billing.domain.invoice.PaymentMethod;
import com.lutz.algashop.billing.domain.invoice.PaymentSettings;
import com.lutz.algashop.billing.domain.utils.FieldValidations;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
public class Payment {
	private String gatewayCode;
	private UUID invoiceId;
	private PaymentMethod method;
	private PaymentSettings settings;
	private PaymentStatus status;

	public Payment(String gatewayCode, @NonNull UUID invoiceId, @NonNull PaymentMethod method,  PaymentSettings settings, @NonNull PaymentStatus status) {
		FieldValidations.requiresNonBlank(gatewayCode);
		this.gatewayCode = gatewayCode;
		this.invoiceId = invoiceId;
		this.method = method;
		this.settings = settings;
		this.status = status;
	}
}
