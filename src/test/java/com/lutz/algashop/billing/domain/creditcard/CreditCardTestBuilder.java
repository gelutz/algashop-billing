package com.lutz.algashop.billing.domain.creditcard;

import java.util.UUID;

public class CreditCardTestBuilder {
	private UUID customerId = UUID.randomUUID();
	private String lastNumber = "1234";
	private String brand = "Mastercard";
	private Integer expMonth = 12;
	private Integer expYear = 2050;
	private String gatewayCreditCardCode = "1";

	private CreditCardTestBuilder() {}

	public static CreditCardTestBuilder aCreditCard() {
		return new CreditCardTestBuilder();
	}

	public CreditCard build() {
		return CreditCard.brandNew(
				customerId,
				lastNumber,
				brand,
				expMonth,
				expYear,
				gatewayCreditCardCode
		);
	}


	public CreditCardTestBuilder customerId(UUID customerId) {
		this.customerId = customerId;
		return this;
	}

	public CreditCardTestBuilder lastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
		return this;
	}

	public CreditCardTestBuilder brand(String brand) {
		this.brand = brand;
		return this;
	}

	public CreditCardTestBuilder expMonth(Integer expMonth) {
		this.expMonth = expMonth;
		return this;
	}

	public CreditCardTestBuilder expYear(Integer expYear) {
		this.expYear = expYear;
		return this;
	}

	public CreditCardTestBuilder gatewayCreditCardCode(String gatewayCreditCardCode) {
		this.gatewayCreditCardCode = gatewayCreditCardCode;
		return this;
	}
}
