package com.lutz.algashop.billing.application.invoice;

import com.lutz.algashop.billing.domain.creditcard.CreditCardNotFoundException;
import com.lutz.algashop.billing.domain.creditcard.CreditCardRepository;
import com.lutz.algashop.billing.domain.invoice.*;
import com.lutz.algashop.billing.domain.invoice.payment.PaymentGatewayService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceManagementApplicationService {
	private final PaymentGatewayService paymentGatewayService;
	private final InvoicingService invoicingService;
	private final InvoiceRepository invoiceRepository;
	private final CreditCardRepository creditCardRepository;

	@Transactional
	public UUID generate(GenerateInvoiceInput input) {
		PaymentSettingsInput paymentSettings = input.getPaymentSettings();
		verifyCreditCardId(paymentSettings.getCreditCardId(), input.getCustomerId());

		Payer payer = convertToPayer(input.getPayer());

		Set<LineItem> items = convertToLineItems(input.getItems());
		Invoice invoice = invoicingService.issue(input.getOrderId(), input.getCustomerId(), payer, items);
		invoice.changePaymentSettings(paymentSettings.getMethod(), paymentSettings.getCreditCardId());

		invoiceRepository.saveAndFlush(invoice);

		return invoice.getId();
	}

	private Set<LineItem> convertToLineItems(Set<LineItemInput> itemsInput) {
		Set<LineItem> items = new LinkedHashSet<>();
		int itemNumber = 1;
		for (LineItemInput itemInput : itemsInput) {
			items.add(LineItem
					          .builder()
					          .number(itemNumber)
					          .name(itemInput.getName())
					          .amount(itemInput.getAmount())
					          .build());
			itemNumber++;
		}

		return items;
	}

	private Payer convertToPayer(PayerData payerData) {
		AddressData addressData = payerData.getAddress();
		return Payer
				.builder()
				.fullName(payerData.getFullName())
				.email(payerData.getEmail())
				.document(payerData.getDocument())
				.phone(payerData.getPhone())
				.address(Address
						         .builder()
						         .street(addressData.getStreet())
						         .number(addressData.getNumber())
						         .complement(addressData.getComplement())
						         .neighborhood(addressData.getNeighborhood())
						         .city(addressData.getCity())
						         .state(addressData.getState())
						         .zipCode(addressData.getZipCode())
						         .build())
				.build();
	}

	private void verifyCreditCardId(UUID creditCardId, @NonNull UUID customerId) {
		if (creditCardId != null && !creditCardRepository.existsByIdAndCustomerId(creditCardId, customerId)) {
			throw new CreditCardNotFoundException();
		}
	}
}
