package com.backend.flowershop.interfaces.controller.seller.mapper;

import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.seller.SellerDocument;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import com.backend.flowershop.interfaces.controller.seller.dto.SellerOnboardingResponse;
import com.backend.flowershop.interfaces.controller.seller.dto.SubmitSellerOnboardingRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 作用：Seller Onboarding DTO <-> Domain 映射
 * 边界：只做转换，不做校验
 */
@Component
public class SellerOnboardingMapper {

    public SellerProfile toProfile(SubmitSellerOnboardingRequest req) {
        Address address = new Address(
                req.addressLine1(),
                req.addressLine2(),
                req.city(),
                req.state(),
                req.postcode(),
                req.country()
        );

        return new SellerProfile(req.companyName(), req.phone(), address);
    }

    public List<SellerDocument> toDocuments(SubmitSellerOnboardingRequest req) {
        if (req.documents() == null) return List.of();

        List<SellerDocument> list = new ArrayList<>();
        for (SubmitSellerOnboardingRequest.SellerDocumentItem item : req.documents()) {
            if (item == null) continue;
            list.add(new SellerDocument(item.type(), item.number(), item.url()));
        }
        return list;
    }

    public SellerOnboardingResponse toResponse(SellerOnboarding onboarding) {
        SellerProfile p = onboarding.profile();
        Address a = p == null ? null : p.address();

        List<SellerOnboardingResponse.DocumentDto> docs = new ArrayList<>();
        List<SellerDocument> sourceDocs = onboarding.documents();
        if (sourceDocs != null) {
            for (SellerDocument d : sourceDocs) {
                docs.add(new SellerOnboardingResponse.DocumentDto(d.type(), d.number(), d.url()));
            }
        }

        return new SellerOnboardingResponse(
                onboarding.userSub(),
                onboarding.status() == null ? null : onboarding.status().name(),
                p == null ? null : p.companyName(),
                p == null ? null : p.phone(),
                new SellerOnboardingResponse.AddressDto(
                        a == null ? null : a.line1(),
                        a == null ? null : a.line2(),
                        a == null ? null : a.city(),
                        a == null ? null : a.state(),
                        a == null ? null : a.postcode(),
                        a == null ? null : a.country()
                ),
                docs
        );
    }
}
