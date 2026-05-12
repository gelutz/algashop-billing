package com.lutz.algashop.billing.application.invoice;


import com.lutz.algashop.billing.domain.creditcard.CreditCard;
import com.lutz.algashop.billing.domain.creditcard.CreditCardRepository;
import com.lutz.algashop.billing.domain.creditcard.CreditCardTestBuilder;
import com.lutz.algashop.billing.domain.invoice.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class InvoiceManagementApplicationServiceIT {
	@Autowired
	private InvoiceManagementApplicationService sut;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private CreditCardRepository creditCardRepository;

	@MockitoSpyBean
	private InvoicingService invoicingServiceSpy;


	@Test
	public void shouldGenerateInvoice() {
		UUID customerId = UUID.randomUUID();
		CreditCard creditCard = CreditCardTestBuilder.aCreditCard()
				.customerId(customerId)
		                                             .build();
		creditCardRepository.saveAndFlush(creditCard);

		GenerateInvoiceInput generateInvoiceInput = GenerateInvoiceInputTestBuilder.anInput()
		                                                                           .customerId(customerId)
		                                                                           .build();

		generateInvoiceInput.setPaymentSettings(
				PaymentSettingsInput.builder()
						.creditCardId(creditCard.getId())
						.method(PaymentMethod.CREDIT_CARD)
				                    .build()
		);

		UUID invoiceId = sut.generate(generateInvoiceInput);

		Invoice invoice  = invoiceRepository.findById(invoiceId).orElseThrow();

		Assertions.assertEquals(InvoiceStatus.UNPAID, invoice.getStatus());
		Assertions.assertEquals(generateInvoiceInput.getOrderId(), invoice.getOrderId());

		verify(invoicingServiceSpy).issue(anyString(), any(UUID.class), any(), any());
	}

	@Test
	public void shouldGenerateInvoiceWithGatewayBalanceAsPayment() {
		UUID customerId = UUID.randomUUID();
		GenerateInvoiceInput generateInvoiceInput = GenerateInvoiceInputTestBuilder.anInput()
		                                                                           .customerId(customerId)
		                                                                           .build();

		generateInvoiceInput.setPaymentSettings(
				PaymentSettingsInput.builder()
				                    .method(PaymentMethod.GATEWAY_BALANCE)
				                    .build()
		);

		UUID invoiceId = sut.generate(generateInvoiceInput);

		Invoice invoice  = invoiceRepository.findById(invoiceId).orElseThrow();

		Assertions.assertEquals(InvoiceStatus.UNPAID, invoice.getStatus());
		Assertions.assertEquals(generateInvoiceInput.getOrderId(), invoice.getOrderId());

		verify(invoicingServiceSpy).issue(anyString(), any(UUID.class), any(), any());

	}
}