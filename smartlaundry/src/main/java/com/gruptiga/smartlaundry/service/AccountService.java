package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.*;
import com.gruptiga.smartlaundry.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    Account getById(String accountId);
    Account getByEmail(String email);
    List<AccountResponse> getAllAccounts(SearchAccountRequest accountRequest);
    long count();
    void updateAccount(String email, AccountRequest request);
    Page<CustomerResponse> getCustomersByEmailAndKeyword(String email, String keyword, int page, int size);

    Page<ServiceTypeResponse> getServiceTypesByEmailAndKeyword(String email, String keyword, int page, int size);

    Page<ServicesResponse> findServicesByAccountEmailAndKeyword(String email, String keyword, int page, int size);

    Page<TypeResponse> findTypeByAccountEmailAndKeyword(String email, String keyword, int page, int size);

}
