package com.lutz.algashop.billing.infrastructure.listener;

import com.lutz.algashop.billing.domain.InvoiceCanceledEvent;
import com.lutz.algashop.billing.domain.InvoiceIssuedEvent;
import com.lutz.algashop.billing.domain.InvoicePaidEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InvoiceEventListener {

    @EventListener
    public void listen(InvoiceIssuedEvent event) {}

    @EventListener
    public void listen(InvoicePaidEvent event) {}

    @EventListener
    public void listen(InvoiceCanceledEvent event) {}
}
