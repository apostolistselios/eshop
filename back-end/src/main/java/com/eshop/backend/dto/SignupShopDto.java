package com.eshop.backend.dto;

public class SignupShopDto {
    private String tin;
    private String brandName;
    private String owner;
    private String email;
    private String password;

    public SignupShopDto(String tin, String brandName, String owner, String email, String password) {
        this.tin = tin;
        this.brandName = brandName;
        this.owner = owner;
        this.email = email;
        this.password = password;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
