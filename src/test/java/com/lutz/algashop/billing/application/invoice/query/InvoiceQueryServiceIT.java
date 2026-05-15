package com.lutz.algashop.billing.application.invoice.query;

import com.lutz.algashop.billing.application.invoice.query.InvoiceOutput;
import com.lutz.algashop.billing.domain.invoice.Invoice;
import com.lutz.algashop.billing.domain.invoice.InvoiceRepository;
import com.lutz.algashop.billing.domain.invoice.InvoiceTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class InvoiceQueryServiceIT {

    @Autowired
    private InvoiceQueryService sut;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void shouldFindByOrderId() {
        Invoice invoice = InvoiceTestBuilder.anInvoice().build();
        invoiceRepository.saveAndFlush(invoice);

        InvoiceOutput output = sut.findByOrderId(invoice.getOrderId());

        Assertions.assertEquals(invoice.getId(), output.getId());
    }
}
