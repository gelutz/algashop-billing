package com.lutz.algashop.billing.domain.invoice;

import com.lutz.algashop.billing.domain.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.lutz.algashop.billing.domain.invoice.InvoiceTestDataBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvoiceTest {

    @Test
    void issue_shouldCreateInvoiceWithCorrectData() {
        UUID customerId = UUID.randomUUID();
        Set<LineItem> items = Set.of(aLineItem(), aLineItemAlt());

        Invoice invoice = Invoice.issue("ORDER-001", customerId, aPayer(), items);

        assertThat(invoice.getId()).isNotNull();
        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.UNPAID);
        assertThat(invoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    void issue_shouldHaveNullTimestampsExceptIssuedAt() {
        Invoice invoice = anInvoice().build();

        assertThat(invoice.getIssuedAt()).isNotNull();
        assertThat(invoice.getPaidAt()).isNull();
        assertThat(invoice.getCanceledAt()).isNull();
        assertThat(invoice.getCancelReason()).isNull();
    }

    @Test
    void markAsPaid_shouldChangeStatusAndSetPaidAt() {
        Invoice invoice = anInvoice().build();

        invoice.markAsPaid();

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PAID);
        assertThat(invoice.getPaidAt()).isNotNull();
    }

    @Test
    void cancel_shouldChangeStatusSetTimestampAndReason() {
        Invoice invoice = anInvoice().build();
        String reason = "Customer requested cancellation";

        invoice.cancel(reason);

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.CANCELED);
        assertThat(invoice.getCanceledAt()).isNotNull();
        assertThat(invoice.getCancelReason()).isEqualTo(reason);
    }

    @Test
    void changePaymentSettings_shouldUpdateMethodAndCreditCardId() {
        Invoice invoice = anInvoice().build();
        UUID creditCardId = UUID.randomUUID();

        invoice.changePaymentSettings(PaymentMethod.CREDIT_CARD, creditCardId);

        assertThat(invoice.getPaymentSettings()).isNotNull();
        assertThat(invoice.getPaymentSettings().getMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
        assertThat(invoice.getPaymentSettings().getCreditCardId()).isEqualTo(creditCardId);
    }

    @Test
    void assignPaymentGatewayCode_shouldSetGatewayCode() {
        Invoice invoice = anInvoice()
                .paymentSettings(PaymentMethod.CREDIT_CARD, UUID.randomUUID())
                .build();

        invoice.assignPaymentGatewayCode("gateway-code-123");

        assertThat(invoice.getPaymentSettings().getGatewayCode()).isEqualTo("gateway-code-123");
    }

    @Test
    void issue_withEmptyItems_shouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> Invoice.issue("ORDER-001", UUID.randomUUID(), aPayer(), new HashSet<>()));
    }

    @Test
    void markAsPaid_whenCanceled_shouldThrow() {
        Invoice invoice = anInvoice().status(InvoiceStatus.CANCELED).build();

        assertThrows(DomainException.class, invoice::markAsPaid);
    }

    @Test
    void cancel_whenAlreadyCanceled_shouldThrow() {
        Invoice invoice = anInvoice().status(InvoiceStatus.CANCELED).build();

        assertThrows(DomainException.class, () -> invoice.cancel("another reason"));
    }

    @Test
    void changePaymentSettings_whenPaid_shouldThrow() {
        Invoice invoice = anInvoice().status(InvoiceStatus.PAID).build();

        assertThrows(DomainException.class,
                () -> invoice.changePaymentSettings(PaymentMethod.CREDIT_CARD, UUID.randomUUID()));
    }

    @Test
    void assignPaymentGatewayCode_whenPaid_shouldThrow() {
        Invoice invoice = anInvoice().status(InvoiceStatus.PAID).build();

        assertThrows(DomainException.class,
                () -> invoice.assignPaymentGatewayCode("gateway-code-123"));
    }

    @Test
    void getItems_shouldReturnImmutableSet() {
        Invoice invoice = anInvoice().build();

        assertThrows(UnsupportedOperationException.class,
                () -> invoice.getItems().clear());
    }
}
