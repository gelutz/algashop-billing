package com.lutz.algashop.billing.domain.invoice;

import com.lutz.algashop.billing.shared.utils.IdGenerator;
import lombok.*;

import java.util.UUID;


@Setter(AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentSettings {
	@EqualsAndHashCode.Include
	private UUID id;
	private UUID creditCardId;
	private String gatewayCode;
	private PaymentMethod method;

	public static PaymentSettings brandNew(PaymentMethod method, UUID creditCardId) {
		return new PaymentSettings(
				IdGenerator.generateTimeBasedUUID(),
				creditCardId,
				null,
				method
		);
	}
}

