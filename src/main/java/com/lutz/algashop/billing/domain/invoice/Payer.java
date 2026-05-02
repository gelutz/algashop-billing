package com.lutz.algashop.billing.domain.invoice;

import lombok.*;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Payer {
	private String fullName;
	private String document;
	private String phone;
	private String email;
	private Address address;
}
