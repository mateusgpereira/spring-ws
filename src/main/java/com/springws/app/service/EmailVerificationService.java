package com.springws.app.service;

import com.springws.app.shared.dto.UserDto;

public interface EmailVerificationService {
    public void verifyEmail(UserDto userDto);
}
