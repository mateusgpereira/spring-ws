package com.springws.app.ui.model.request;

import lombok.Data;

@Data
public class PasswordResetModel {

    private String token;

    private String password;
}
