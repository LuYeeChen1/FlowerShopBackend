package com.backend.flowershop.infrastructure.integration;

import com.backend.flowershop.application.port.integration.SellerOnboardingLambdaPort;
import com.backend.flowershop.domain.model.SellerOnboarding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpSellerOnboardingLambdaClient implements SellerOnboardingLambdaPort {
    private final RestTemplate restTemplate;
    private final String lambdaUrl;

    public HttpSellerOnboardingLambdaClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${integration.lambda.seller-onboarding-url}") String lambdaUrl
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.lambdaUrl = lambdaUrl;
    }

    @Override
    public void invoke(SellerOnboarding onboarding) {
        SellerOnboardingPayload payload = new SellerOnboardingPayload(
                onboarding.id().value(),
                onboarding.storeName(),
                onboarding.contact().name(),
                onboarding.contact().email(),
                onboarding.contact().phone(),
                onboarding.status().name(),
                onboarding.createdAt().toString()
        );
        restTemplate.postForEntity(lambdaUrl, payload, Void.class);
    }

    private record SellerOnboardingPayload(
            String id,
            String storeName,
            String contactName,
            String contactEmail,
            String contactPhone,
            String status,
            String createdAt
    ) {
    }
}
