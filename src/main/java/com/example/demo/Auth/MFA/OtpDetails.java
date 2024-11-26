package com.example.demo.Auth.MFA;

public class OtpDetails {
    private final String otp; // Non-static, instance-specific field
    private final long expiry;

    public OtpDetails(String otp, long expiry) {
        this.otp = otp;
        this.expiry = expiry;
    }

    public String getOtp() {
        return otp;
    }

    public long getExpiry() {
        return expiry;
    }
}
