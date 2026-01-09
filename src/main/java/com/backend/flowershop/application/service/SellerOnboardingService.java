package com.backend.flowershop.application.service;

import com.backend.flowershop.application.port.integration.SellerOnboardingLambdaPort;
import com.backend.flowershop.application.port.persistence.SellerOnboardingRepository;
import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import com.backend.flowershop.application.port.usecase.SellerOnboardingUseCase;
import com.backend.flowershop.domain.error.DomainErrorException;
import com.backend.flowershop.domain.model.SellerContact;
import com.backend.flowershop.domain.model.SellerOnboarding;
import com.backend.flowershop.domain.model.SellerOnboardingId;
import com.backend.flowershop.domain.model.SellerOnboardingStatus;
import com.backend.flowershop.validation.SellerOnboardingValidator;
import com.backend.flowershop.validation.ValidationError;
import com.backend.flowershop.validation.ValidationResult;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SellerOnboardingService implements SellerOnboardingUseCase {
    private static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

    private final SellerOnboardingValidator validator;
    private final SellerOnboardingRepository repository;
    private final SellerOnboardingLambdaPort lambdaPort;

    public SellerOnboardingService(
            SellerOnboardingValidator validator,
            SellerOnboardingRepository repository,
            SellerOnboardingLambdaPort lambdaPort
    ) {
        this.validator = validator;
        this.repository = repository;
        this.lambdaPort = lambdaPort;
    }

    @Override
    public SellerOnboarding submit(SellerOnboardingCommand command) {
        ValidationResult validationResult = validator.validate(command);
        if (!validationResult.isValid()) {
            throw new DomainErrorException(VALIDATION_ERROR_CODE, buildValidationMessage(validationResult));
        }
        SellerOnboarding onboarding = new SellerOnboarding(
                SellerOnboardingId.newId(),
                normalize(command.storeName()),
                new SellerContact(
                        normalize(command.contactName()),
                        normalize(command.contactEmail()),
                        normalize(command.contactPhone())
                ),
                SellerOnboardingStatus.PENDING_REVIEW,
                OffsetDateTime.now(ZoneOffset.UTC)
        );
        repository.save(onboarding);
        lambdaPort.invoke(onboarding);
        return onboarding;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    private String buildValidationMessage(ValidationResult result) {
        return result.errors().stream()
                .map(this::formatError)
                .collect(Collectors.joining("; "));
    }

    private String formatError(ValidationError error) {
        return error.code() + ": " + error.message();
    }
}
