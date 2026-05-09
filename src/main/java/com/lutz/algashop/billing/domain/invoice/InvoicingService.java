package com.lutz.algashop.billing.domain.invoice;

import com.lutz.algashop.billing.domain.DomainException;
import com.lutz.algashop.billing.domain.invoice.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoicingService {
	private final InvoiceRepository invoiceRepository;

	public Invoice issue(String orderId, UUID customerId, Payer payer, Set<LineItem> items) {
		if (invoiceRepository.existsByOrderId(orderId)) {
			throw new DomainException("invoice pra essa order já existe");
		}

		return Invoice.issue(orderId, customerId, payer, items);
	}

	public void assignPayment(Invoice invoice, Payment payment) {
		invoice.assignPaymentGatewayCode(payment.getGatewayCode());
		switch (payment.getStatus()) {
			case PAID -> invoice.markAsPaid();
			case FAILED -> invoice.cancel("Payment failed.");
			case REFUNDED -> invoice.cancel("Payment refunded.");
		}

	}
}
