package com.lutz.algashop.billing.domain.invoice;

import com.lutz.algashop.billing.domain.AbstractAuditableAggregateRoot;
import com.lutz.algashop.billing.domain.DomainException;
import com.lutz.algashop.billing.domain.InvoiceCanceledEvent;
import com.lutz.algashop.billing.domain.InvoiceIssuedEvent;
import com.lutz.algashop.billing.domain.InvoicePaidEvent;
import com.lutz.algashop.billing.domain.utils.IdGenerator;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Setter(AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Invoice extends AbstractAuditableAggregateRoot<Invoice> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String orderId;
    private UUID customerId;

    private OffsetDateTime issuedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime expiresAt;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToOne(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        orphanRemoval = true
    )
    private PaymentSettings paymentSettings;

    @ElementCollection
    @CollectionTable(
        name = "invoice_line_item",
        joinColumns = @JoinColumn(name = "invoice_id")
    )
    private Set<LineItem> items = new HashSet<>();

    @Embedded
    private Payer payer;

    private String cancelReason;

    public static Invoice issue(
        String orderId,
        @NonNull UUID customerId,
        @NonNull Payer payer,
        @NonNull Set<LineItem> items
    ) {
        if (StringUtils.isBlank(orderId)) {
            throw new IllegalArgumentException(); // pode ser nulo, não pode ser uma string vazia
        }

        if (items.isEmpty()) {
            throw new IllegalArgumentException(); // pode ser nulo, não pode ser uma string vazia
        }

        BigDecimal invoiceAmount = items
            .stream()
            .map(LineItem::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Invoice invoice = new Invoice(
            IdGenerator.generateTimeBasedUUID(),
            orderId,
            customerId,
            OffsetDateTime.now(),
            null,
            null,
            OffsetDateTime.now().plusDays(3),
            invoiceAmount,
            InvoiceStatus.UNPAID,
            null,
            items,
            payer,
            null
        );

        invoice.registerEvent(
            new InvoiceIssuedEvent(
                invoice.getId(),
                invoice.getCustomerId(),
                invoice.getOrderId(),
                invoice.getIssuedAt()
            )
        );

        return invoice;
    }

    public Set<LineItem> getItems() {
        return Collections.unmodifiableSet(this.items);
    }

    public boolean isCanceled() {
        return InvoiceStatus.CANCELED.equals(this.getStatus());
    }

    public boolean isUnpaid() {
        return InvoiceStatus.UNPAID.equals(this.getStatus());
    }

    public boolean isPaid() {
        return InvoiceStatus.PAID.equals(this.getStatus());
    }

    public void markAsPaid() {
        if (!isUnpaid()) {
            throw new DomainException(
                String.format(
                    "Invoice %s with status %s cannot be marked as paid.",
                    this.getId(),
                    this.getStatus().toString().toLowerCase()
                )
            );
        }

        setPaidAt(OffsetDateTime.now());
        setStatus(InvoiceStatus.PAID);
        registerEvent(
            new InvoicePaidEvent(
                getId(),
                getCustomerId(),
                getOrderId(),
                getPaidAt()
            )
        );
    }

    public void cancel(String cancelReason) {
        if (isCanceled()) {
            throw new DomainException(
                String.format("Invoice %s is already canceled", this.getId())
            );
        }

        setCanceledAt(OffsetDateTime.now());
        setStatus(InvoiceStatus.CANCELED);
        setCancelReason(cancelReason);
        registerEvent(
            new InvoiceCanceledEvent(
                getId(),
                getCustomerId(),
                getOrderId(),
                getCanceledAt()
            )
        );
    }

    public void assignPaymentGatewayCode(String code) {
        if (!isUnpaid()) {
            throw new DomainException(); // todo: quando ver isso de novo, cria um arquivo que gera as mensagens de erro igual no ordering
        }

        this.getPaymentSettings().assignGatewayCode(code);
    }

    public void changePaymentSettings(PaymentMethod method, UUID creditCardId) {
        if (!isUnpaid()) {
            throw new DomainException(
                String.format(
                    "Invoice %s with status %s cannot have payment settings changed.",
                    this.getId(),
                    this.getStatus().toString().toLowerCase()
                )
            );
        }
        PaymentSettings paymentSettings = PaymentSettings.brandNew(
            method,
            creditCardId
        );
        paymentSettings.setInvoice(this);
        this.setPaymentSettings(paymentSettings);
    }
}
