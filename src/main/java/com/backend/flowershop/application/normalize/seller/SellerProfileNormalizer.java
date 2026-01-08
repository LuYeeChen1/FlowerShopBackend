package com.backend.flowershop.application.normalize.seller;

import com.backend.flowershop.application.normalize.common.PhoneNormalizer;
import com.backend.flowershop.application.normalize.common.TextNormalizer;
import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import org.springframework.stereotype.Component;

/**
 * 作用：SellerProfile 标准化（Normalize only）
 * 边界：只做格式化，不做任何 validate/业务判断
 */
@Component
public class SellerProfileNormalizer {

    private final TextNormalizer textNormalizer;
    private final PhoneNormalizer phoneNormalizer;

    public SellerProfileNormalizer(TextNormalizer textNormalizer, PhoneNormalizer phoneNormalizer) {
        this.textNormalizer = textNormalizer;
        this.phoneNormalizer = phoneNormalizer;
    }

    public SellerProfile normalize(SellerProfile input) {
        if (input == null) return null;

        Address a = input.address();
        Address na = null;
        if (a != null) {
            na = new Address(
                    textNormalizer.normalize(a.line1()),
                    textNormalizer.normalize(a.line2()),
                    textNormalizer.normalize(a.city()),
                    textNormalizer.normalize(a.state()),
                    textNormalizer.normalize(a.postcode()),
                    textNormalizer.normalize(a.country())
            );
        }

        return new SellerProfile(
                textNormalizer.normalize(input.companyName()),
                phoneNormalizer.normalize(input.phone()),
                na
        );
    }
}
