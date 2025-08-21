package com.training.moneybags.dtos;

import java.util.List;

public record UserCreateRequest(String username, String password, List<String> roles) {}

