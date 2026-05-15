package com.lutz.algashop.billing.infrastructure.persistence.invoice;

import com.lutz.algashop.billing.application.invoice.InvoiceNotFoundException;
import com.lutz.algashop.billing.application.invoice.query.InvoiceOutput;
import com.lutz.algashop.billing.application.invoice.query.InvoiceQueryService;
import com.lutz.algashop.billing.application.utility.Mapper;
import com.lutz.algashop.billing.domain.invoice.Invoice;
import com.lutz.algashop.billing.domain.invoice.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvoiceQueryServiceImpl implements InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;
    private final Mapper mapper;

    @Override
    public InvoiceOutput findByOrderId(String orderId) {
        Invoice invoice = invoiceRepository
            .findByOrderId(orderId)
            .orElseThrow(() -> new InvoiceNotFoundException());

        return mapper.convert(invoice, InvoiceOutput.class);
    }
}
