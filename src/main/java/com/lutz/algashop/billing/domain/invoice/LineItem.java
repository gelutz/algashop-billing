package com.lutz.algashop.billing.domain.invoice;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class LineItem {
	private Integer number;
	private String name;
	private BigDecimal amount;
}
