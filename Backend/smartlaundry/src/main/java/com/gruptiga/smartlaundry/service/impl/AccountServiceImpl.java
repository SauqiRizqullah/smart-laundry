package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.EmailPattern;
import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.response.*;
import com.gruptiga.smartlaundry.entity.*;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.repository.CustomerRepository;
import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
import com.gruptiga.smartlaundry.repository.TransactionRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.ImageService;
import com.gruptiga.smartlaundry.specification.AccountSpecification;
import com.gruptiga.smartlaundry.validation.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountValidator accountValidator;

    private final ImageService imageService;




    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = Account.builder()
                .name(accountRequest.getName())
                .address(accountRequest.getAddress())
                .contact(accountRequest.getContact())
                .email(accountRequest.getEmail())
                .password(accountRequest.getPassword())
                .build();

        accountRepository.saveAndFlush(account);

        return parseAccountToAccountResponse(account);
    }

    private AccountResponse parseAccountToAccountResponse(Account account) {
        String id = account.getAccountId() == null ? null : account.getAccountId();

        return AccountResponse.builder()
                .accountId(id)
                .name(account.getName())
                .address(account.getAddress())
                .contact(account.getContact())
                .email(account.getEmail())
                .build();
    }

    @Override
    public Account getById(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Account tidak ditemukan!!!"));
    }

    @Override
    public Account getByEmail(String email) {
        log.info("Fetching account by email: {}", email);
        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isPresent()) {
            log.info("Account found: {}", accountOpt.get());
        } else {
            log.warn("Account with email {} not found", email);
        }
        return accountOpt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email Account tidak ditemukan!!!"));
    }


    @Override
    @Transactional
    public void updateAccount(String email, AccountRequest request) {

        Image image = imageService.create(request.getImage());


        accountValidator.validateAccountRequest(request);



        Optional<Account> existingAccountOptional = accountRepository.findByEmail(email);
        if (!existingAccountOptional.isPresent()) {
            throw new IllegalArgumentException("Account does not exist");
        }

        Account account = existingAccountOptional.get();

        if (!email.equals(request.getEmail()) && accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("New email is already taken");
        }

        if (!EmailPattern.EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new IllegalArgumentException("New email format is invalid");
        }

        Image oldImage = account.getImage();

//        String hashPassword = passwordEncoder.encode(request.getPassword());
//
//        accountRepository.updateAccount(
//                email,
//                request.getName(),
//                request.getAddress(),
//                request.getContact(),
//                hashPassword,
//                image
//        );

        account.setImage(image);
        account.setName(request.getName());
        account.setAddress(request.getAddress());
        account.setContact(request.getContact());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        accountRepository.save(account);

        if (oldImage != null){
            imageService.delete(oldImage.getImageId());
        }
    }





    @Override
    public List<AccountResponse> getAllAccounts(SearchAccountRequest accountRequest) {
        Specification<Account> accountSpecification = AccountSpecification.getSpecification(accountRequest);
        if (accountRequest.getName() == null) {
            return accountRepository.findAll().stream()
                    .map(this::parseAccountToAccountResponse)
                    .toList();
        } else {
            return accountRepository.findAll(accountSpecification).stream()
                    .map(this::parseAccountToAccountResponse)
                    .toList();
        }
    }

    @Override
    public long count() {
        return accountRepository.count();
    }



    @Override
    public Page<CustomerResponse> getCustomersByEmailAndKeyword(String email, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = accountRepository.findCustomersByAccountEmailAndKeyword(email, keyword, pageable);

        List<CustomerResponse> customerResponses = customerPage.stream()
                .map(c -> CustomerResponse.builder()
                        .customerId(c.getCustomerId())
                        .name(c.getName())
                        .address(c.getAddress())
                        .phoneNumber(c.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(customerResponses, pageable, customerPage.getTotalElements());
    }

    @Override
    public Page<ServiceTypeResponse> getServiceTypesByEmailAndKeyword(String email, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceType> serviceTypePage = accountRepository.findServiceTypesByAccountEmailAndKeyword(email, keyword, pageable);

        List<ServiceTypeResponse> serviceTypeResponses = serviceTypePage.stream()
                .map(s -> ServiceTypeResponse.builder()
                        .serviceTypeId(s.getServiceTypeId())
                        .type(s.getType())
                        .service(s.getService())
                        .price(s.getPrice())
                        .detail(String.valueOf(s.getDetail()))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(serviceTypeResponses, pageable, serviceTypePage.getTotalElements());
    }

    @Override
    public Page<ServicesResponse> findServicesByAccountEmailAndKeyword(String email, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<com.gruptiga.smartlaundry.entity.Service> services = accountRepository.findServicesByAccountEmailAndKeyword(email, keyword, pageable);

        List<ServicesResponse> servicesResponses = services.stream()
                .map(s -> ServicesResponse.builder()
                        .serviceId(s.getServiceId())
                        .name(s.getName())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(servicesResponses, pageable, services.getTotalElements());
    }

    @Override
    public Page<TypeResponse> findTypeByAccountEmailAndKeyword(String email, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Type> types = accountRepository.findTypesByAccountEmailAndKeyword(email, keyword, pageable);

        List<TypeResponse> typeResponses = types.stream()
                .map(s -> TypeResponse.builder()
                        .typeId(s.getTypeId())
                        .name(s.getName())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(typeResponses, pageable, types.getTotalElements());
    }


}
