package com.lutz.algashop.billing.application.invoice;

import com.lutz.algashop.billing.domain.creditcard.CreditCard;
import com.lutz.algashop.billing.domain.creditcard.CreditCardRepository;
import com.lutz.algashop.billing.domain.creditcard.CreditCardTestBuilder;
import com.lutz.algashop.billing.domain.invoice.*;
import com.lutz.algashop.billing.domain.invoice.payment.Payment;
import com.lutz.algashop.billing.domain.invoice.payment.PaymentGatewayService;
import com.lutz.algashop.billing.domain.invoice.payment.PaymentRequest;
import com.lutz.algashop.billing.domain.invoice.payment.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @MockitoBean
    private InvoicingService invoicingServiceSpy;

    @Test
    public void shouldGenerateInvoice() {
        UUID customerId = UUID.randomUUID();
        CreditCard creditCard = CreditCardTestBuilder.aCreditCard()
            .customerId(customerId)
            .build();
        creditCardRepository.saveAndFlush(creditCard);

        GenerateInvoiceInput generateInvoiceInput =
            GenerateInvoiceInputTestBuilder.anInput()
                .customerId(customerId)
                .build();

        generateInvoiceInput.setPaymentSettings(
            PaymentSettingsInput.builder()
                .creditCardId(creditCard.getId())
                .method(PaymentMethod.CREDIT_CARD)
                .build()
        );

        UUID invoiceId = sut.generate(generateInvoiceInput);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        Assertions.assertEquals(InvoiceStatus.UNPAID, invoice.getStatus());
        Assertions.assertEquals(
            generateInvoiceInput.getOrderId(),
            invoice.getOrderId()
        );

        verify(invoicingServiceSpy).issue(
            anyString(),
            any(UUID.class),
            any(),
            any()
        );
    }

    @Test
    public void shouldGenerateInvoiceWithGatewayBalanceAsPayment() {
        UUID customerId = UUID.randomUUID();
        GenerateInvoiceInput generateInvoiceInput =
            GenerateInvoiceInputTestBuilder.anInput()
                .customerId(customerId)
                .build();

        generateInvoiceInput.setPaymentSettings(
            PaymentSettingsInput.builder()
                .method(PaymentMethod.GATEWAY_BALANCE)
                .build()
        );

        UUID invoiceId = sut.generate(generateInvoiceInput);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        Assertions.assertEquals(InvoiceStatus.UNPAID, invoice.getStatus());
        Assertions.assertEquals(
            generateInvoiceInput.getOrderId(),
            invoice.getOrderId()
        );

        verify(invoicingServiceSpy).issue(
            anyString(),
            any(UUID.class),
            any(),
            any()
        );
    }

    @Test
    public void shouldProcessInvoicePayment() {
        Invoice invoice = InvoiceTestBuilder.anInvoice().build();
        invoiceRepository.saveAndFlush(invoice);

        Payment payment = Payment.builder()
            .gatewayCode("123")
            .invoiceId(invoice.getId())
            .method(invoice.getPaymentSettings().getMethod())
            .status(PaymentStatus.PAID)
            .build();

        when(
            paymentGatewayService.capture(any(PaymentRequest.class))
        ).thenReturn(payment);

        sut.processPayment(invoice.getId());

        Invoice paidInvoice = invoiceRepository
            .findById(invoice.getId())
            .orElseThrow();

        assertTrue(paidInvoice.isPaid());

        verify(paymentGatewayService).capture(any(PaymentRequest.class));
        verify(invoicingServiceSpy).assignPayment(
            any(Invoice.class),
            any(Payment.class)
        );
    }
}
