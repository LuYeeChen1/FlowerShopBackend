package com.backend.flowershop.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SellerApplyDTORequest {

    @NotBlank(message = "Type is required")
    private String applyType; // "INDIVIDUAL" 或 "BUSINESS"

    // --- 公共字段 ---
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Invalid phone format")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address; // 映射到 garden_address 或 business_address

    // --- 个人字段 (Individual) ---
    private String realName;
    private String nricNumber;

    // --- 企业字段 (Business) ---
    private String companyName;
    private String brnNumber; // Registration Number
    private String tinNumber;
    private String msicCode;
    private String sstNumber;

    public @NotBlank(message = "Type is required") String getApplyType() {
        return applyType;
    }

    public void setApplyType(@NotBlank(message = "Type is required") String applyType) {
        this.applyType = applyType;
    }

    public @NotBlank(message = "Phone number is required") @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Invalid phone format") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Phone number is required") @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Invalid phone format") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address is required") String address) {
        this.address = address;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNricNumber() {
        return nricNumber;
    }

    public void setNricNumber(String nricNumber) {
        this.nricNumber = nricNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBrnNumber() {
        return brnNumber;
    }

    public void setBrnNumber(String brnNumber) {
        this.brnNumber = brnNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getMsicCode() {
        return msicCode;
    }

    public void setMsicCode(String msicCode) {
        this.msicCode = msicCode;
    }

    public String getSstNumber() {
        return sstNumber;
    }

    public void setSstNumber(String sstNumber) {
        this.sstNumber = sstNumber;
    }
}