package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Account getByUserId(String id);

    Account getByContext();

    void logout();
}

