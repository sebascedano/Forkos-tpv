package com.forkos.forkos.dto.request;

import lombok.*;

@Data
public class LoginRequest {
    private String username;
    private String password;
}