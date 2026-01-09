package com.backend.flowershop.interfaces.controller;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import com.backend.flowershop.application.port.usecase.SellerOnboardingUseCase;
import com.backend.flowershop.domain.model.SellerOnboarding;
import com.backend.flowershop.interfaces.controller.dto.SellerOnboardingRequest;
import com.backend.flowershop.interfaces.controller.dto.SellerOnboardingResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/onboarding")
public class SellerOnboardingController {
    private final SellerOnboardingUseCase sellerOnboardingUseCase;

    public SellerOnboardingController(SellerOnboardingUseCase sellerOnboardingUseCase) {
        this.sellerOnboardingUseCase = sellerOnboardingUseCase;
    }

    @PostMapping
    public SellerOnboardingResponse submit(@RequestBody SellerOnboardingRequest request) {
        SellerOnboardingCommand command = new SellerOnboardingCommand(
                request.storeName(),
                request.contactName(),
                request.contactEmail(),
                request.contactPhone()
        );
        SellerOnboarding onboarding = sellerOnboardingUseCase.submit(command);
        return new SellerOnboardingResponse(
                onboarding.id().value(),
                onboarding.status().name(),
                onboarding.storeName(),
                onboarding.contact().name(),
                onboarding.contact().email(),
                onboarding.contact().phone()
        );
    }
}
