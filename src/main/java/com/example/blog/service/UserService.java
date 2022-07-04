package com.example.blog.service;

import com.example.blog.model.dto.SignUpDto;

public interface UserService {
    void createUser(SignUpDto userDto);
}
