package com.lutz.algashop.billing.domain.invoice;

import com.lutz.algashop.billing.domain.utils.FieldValidations;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class LineItem {
	private Integer number;
	private String name;
	private BigDecimal amount;

	@Builder
	public LineItem(Integer number, String name, @NonNull BigDecimal amount) {
		FieldValidations.requiresNonBlank(name);
		FieldValidations.requireGreatherThanZero(amount);
		FieldValidations.requireGreatherThanZero(number);

		this.number = number;
		this.name = name;
		this.amount = amount;
	}
}
