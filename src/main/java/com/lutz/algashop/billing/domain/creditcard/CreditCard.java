package com.lutz.algashop.billing.domain.creditcard;

import com.lutz.algashop.billing.domain.utils.IdGenerator;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

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

	private String gatewayCode;

	public static CreditCard brandNew(
			@NonNull UUID customerId,
		    String lastNumbers,
			String brand,
			@NonNull Integer expMonth,
			@NonNull Integer expYear,
			String gatewayCreditCardCode) {

		if (StringUtils.isAnyBlank(lastNumbers, brand, gatewayCreditCardCode)) {
			throw new IllegalArgumentException();
		}
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

	public void setGatewayCode(String gatewayCode) {
		if (StringUtils.isBlank(gatewayCode)) {
			throw new IllegalArgumentException();
		}

		this.gatewayCode = gatewayCode;
	}
}
