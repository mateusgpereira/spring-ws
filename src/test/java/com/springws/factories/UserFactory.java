package com.springws.factories;

import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.request.UserDetailsRequestModel;
import com.springws.test_utils.BaseFaker;

public class UserFactory {

    private static final int PASSWORD_LENGTH = 8;

    private static final BaseFaker.AppFaker faker = new BaseFaker.AppFaker();

    public static UserDetailsRequestModel createDetailsRequestModel() {
        String name = faker.name().firstName();
        return UserDetailsRequestModel.builder()
                .firstName(name)
                .email(faker.mail().random(name))
                .password(faker.text().text(PASSWORD_LENGTH))
                .build();
    }

    public static UserDto createDtoFromDetailsRequestModel(UserDetailsRequestModel details) {
        return UserDto.builder()
                .firstName(details.getFirstName())
                .email(details.getEmail())
                .password(details.getPassword())
                .emailVerificationStatus(false)
                .build();
    }
}
