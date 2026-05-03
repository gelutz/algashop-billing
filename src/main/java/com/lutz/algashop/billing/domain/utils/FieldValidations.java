package com.lutz.algashop.billing.domain.utils;

import lombok.NonNull;
import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigDecimal;
import java.util.Objects;


public class FieldValidations {
	private FieldValidations() {}

	public static void requireGreatherThanZero(Integer amount) {
		requireGreatherThanZero(BigDecimal.valueOf(amount));
	}

	public static void requireGreatherThanZero(@NonNull BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException();
		}
	}

	public static void requiresNonBlank(String value) {
		requiresNonBlank(value, null);
	}

	public static void requiresNonBlank(String value, String errorMessage) {
		Objects.requireNonNull(value);
		if (value.isBlank()) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void requiresValidEmail(String email) {
		requiresValidEmail(email, null);
	}

	public static void requiresValidEmail(String email, String errorMessage) {
		Objects.requireNonNull(email, errorMessage);
		if (email.isBlank()) {
			throw new IllegalArgumentException(errorMessage);
		}
		if (!EmailValidator.getInstance().isValid(email)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}
}

