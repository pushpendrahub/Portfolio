package com.portfolio.pushpendra.model;

import jakarta.persistence.*;

/**
 * Entity to store Google OAuth access & refresh tokens.
 */
@Entity
@Table(name = "google_tokens")
public class GoogleTokenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", length = 1000)
    private String accessToken;

    @Column(name = "refresh_token", length = 1000)
    private String refreshToken;

    @Column(name = "expiry_time")
    private Long expiryTimeMillis; // Store in milliseconds

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public void setExpiryTimeMillis(Long expiryTimeMillis) {
        this.expiryTimeMillis = expiryTimeMillis;
    }
}
