package com.lutz.algashop.billing.infrastructure.payment;

import com.lutz.algashop.billing.domain.invoice.PaymentMethod;
import com.lutz.algashop.billing.domain.invoice.payment.Payment;
import com.lutz.algashop.billing.domain.invoice.payment.PaymentGatewayService;
import com.lutz.algashop.billing.domain.invoice.payment.PaymentRequest;
import com.lutz.algashop.billing.domain.invoice.payment.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentFakeImpl implements PaymentGatewayService {

	@Override
	public Payment capture(PaymentRequest request) {
//		var settings = PaymentSettings.
		return Payment
				.builder()
				.invoiceId(request.getInvoiceId())
				.status(PaymentStatus.PAID)
				.method(request.getMethod())
				.gatewayCode(UUID.randomUUID().toString())
				.build();
	}

	@Override
	public Payment findByCode(String gatewayCode) {
		return Payment
				.builder()
				.invoiceId(UUID.randomUUID())
				.status(PaymentStatus.PAID)
				.method(PaymentMethod.GATEWAY_BALANCE)
				.gatewayCode(UUID.randomUUID().toString())
				.build();
	}
}
