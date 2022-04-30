package com.springws.app.service;

import com.springws.app.shared.dto.UserDto;

public interface EmailVerificationService {
    public void verifyEmail(UserDto userDto);
    boolean sendPasswordResetRequest(String name, String email, String token);
}
