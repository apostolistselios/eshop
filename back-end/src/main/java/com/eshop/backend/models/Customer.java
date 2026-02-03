package com.eshop.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_customer_tin", columnNames = "tin"),
                @UniqueConstraint(name = "uk_customer_email", columnNames = "email")
        }
)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tax identification number
     */
    @NotBlank
    @Size(min = 9, max = 9)
    @Column(nullable = false, length = 9)
    private String tin;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String firstname;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String lastname;

    @NotBlank
    @Email
    @Column(nullable = false, length = 160)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Customer() {
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
