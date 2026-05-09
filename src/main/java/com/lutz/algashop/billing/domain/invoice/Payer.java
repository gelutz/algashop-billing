package com.lutz.algashop.billing.domain.invoice;

import com.lutz.algashop.billing.domain.utils.FieldValidations;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Payer {
	private String fullName;
	private String document;
	private String phone;
	private String email;

	@Embedded
	private Address address;

	@Builder
	public Payer(String fullName, String document, String phone, String email, @NonNull Address address) {
		FieldValidations.requiresNonBlank(fullName);
		FieldValidations.requiresNonBlank(document);
		FieldValidations.requiresValidEmail(email);

		this.fullName = fullName;
		this.document = document;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}
}
