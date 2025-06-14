package com.forkos.forkos.dto.request;
import lombok.Data;

@Data
public class SignupRequest {
    private String nombre;
    private String username;
    private String email;
    private String password;
}
