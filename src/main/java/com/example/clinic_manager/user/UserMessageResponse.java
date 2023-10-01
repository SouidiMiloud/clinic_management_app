package com.example.clinic_manager.user;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserMessageResponse {

    private String name;
    private String imagePath;
}