package org.example.gamearenax_backend.dto;

public class OTPDetails {
    private int code;
    private long expiryTime; // timestamp in millis

    public OTPDetails(int code, long expiryTime) {
        this.code = code;
        this.expiryTime = expiryTime;
    }

    public int getCode() { return code; }
    public long getExpiryTime() { return expiryTime; }
}
