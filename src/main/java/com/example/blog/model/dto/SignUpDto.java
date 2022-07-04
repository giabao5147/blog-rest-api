package com.example.blog.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpDto {
    @NotEmpty
    String name;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;
}
