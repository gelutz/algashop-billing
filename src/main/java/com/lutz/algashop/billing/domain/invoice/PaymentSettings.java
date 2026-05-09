package com.lutz.algashop.billing.domain.invoice;

import com.lutz.algashop.billing.domain.DomainException;
import com.lutz.algashop.billing.domain.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;


@Setter(AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PaymentSettings {
	@Id
	@EqualsAndHashCode.Include
	private UUID id;
	private UUID creditCardId;
	private String gatewayCode;

	@Enumerated(EnumType.STRING)
	private PaymentMethod method;

	@OneToOne(mappedBy = "paymentSettings")
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PACKAGE)
	private Invoice invoice;

	// package private por que só é usado no aggregate (invoice)
	static PaymentSettings brandNew(@NonNull PaymentMethod method, UUID creditCardId) {
		if (method.equals(PaymentMethod.CREDIT_CARD)) {
			Objects.requireNonNull(creditCardId);
		}

		return new PaymentSettings(
				IdGenerator.generateTimeBasedUUID(),
				creditCardId,
				null,
				method,
				null
		);
	}

	void assignGatewayCode(String code) {
		if (StringUtils.isBlank(code)) {
			throw new IllegalArgumentException();
		}

		if (this.getGatewayCode() != null) {
			throw new DomainException("Gateway code already assigned");
		}

		this.setGatewayCode(code);
	}
}

