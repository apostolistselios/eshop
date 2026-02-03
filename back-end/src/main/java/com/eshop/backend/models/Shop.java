package com.eshop.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
        name = "shop",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_shop_tin", columnNames = "tin")
        }
)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tax Identification Number
     */
    @NotBlank
    @Size(min = 9, max = 9)
    @Column(nullable = false, length = 9)
    private String tin;

    @NotBlank
    @Column(nullable = false, length = 160)
    private String brandName;

    @NotBlank
    @Column(nullable = false, length = 160)
    private String owner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Shop() {
    }

    public Long getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
