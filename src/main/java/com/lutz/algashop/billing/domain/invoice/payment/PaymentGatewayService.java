package com.lutz.algashop.billing.domain.invoice.payment;

public interface PaymentGatewayService {
	Payment capture(PaymentRequest request);
	Payment findByCode(String gatewayCode);
}
