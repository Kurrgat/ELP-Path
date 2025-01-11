package com.example.emtechelppathbackend.scholars;

import lombok.Data;

@Data
public class UserCountDto {
    private long registeredUsers;
    private long unregisteredUsers;

    public UserCountDto(Long registeredUsers, Long unregisteredUsers) {
        this.registeredUsers = registeredUsers;
        this.unregisteredUsers = unregisteredUsers;
    }
}
