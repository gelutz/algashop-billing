package com.lutz.algashop.billing.domain.creditcard;

import com.lutz.algashop.billing.shared.utils.IdGenerator;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter(AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditCard {

	@EqualsAndHashCode.Include
	private UUID id;
	private OffsetDateTime createdAt;
	private UUID customerId;

	private String lastNumbers;
	private String brand;
	private Integer expMonth;
	private Integer expYear;

	@Setter(AccessLevel.PUBLIC)
	private String gatewayCode;

	public static CreditCard brandNew(UUID customerId, String lastNumbers, String brand, Integer expMonth, Integer expYear, String gatewayCreditCardCode) {
		return new CreditCard(
				IdGenerator.generateTimeBasedUUID(),
				OffsetDateTime.now(),
				customerId,
				lastNumbers,
				brand,
				expMonth,
				expYear,
				gatewayCreditCardCode
		);
	}
}
